/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.session

import de.justjanne.libquassel.client.util.CoroutineKeyedQueue
import de.justjanne.libquassel.protocol.exceptions.HandshakeException
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.serializers.HandshakeMessageSerializer
import de.justjanne.libquassel.protocol.session.CoreState
import de.justjanne.libquassel.protocol.session.HandshakeHandler
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.util.log.trace
import de.justjanne.libquassel.protocol.variant.QVariantMap
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

class ClientHandshakeHandler(
  val session: Session
) : HandshakeHandler, ClientConnectionHandler() {
  private val messageQueue = CoroutineKeyedQueue<Class<out HandshakeMessage>, HandshakeMessage>()
  private var sessionInit: HandshakeMessage.SessionInit? = null

  override suspend fun read(buffer: ByteBuffer): Boolean {
    return dispatch(HandshakeMessageSerializer.deserialize(buffer, channel!!.negotiatedFeatures))
  }

  private fun dispatch(message: HandshakeMessage): Boolean {
    logger.trace { "Read handshake message $message" }
    messageQueue.resume(message.javaClass, message)
    if (message is HandshakeMessage.SessionInit) {
      sessionInit = message
      return true
    }
    return false
  }

  override suspend fun done() {
    super.done()
    val message = sessionInit
    if (message != null) {
      session.init(message.identities, message.bufferInfos, message.networkIds)
    }
  }

  override suspend fun init(
    clientVersion: String,
    buildDate: String,
    featureSet: FeatureSet
  ): CoreState {
    when (
      val response = messageQueue.wait(
        HandshakeMessage.ClientInitAck::class.java,
        HandshakeMessage.ClientInitReject::class.java
      ) {
        emit(HandshakeMessage.ClientInit(clientVersion, buildDate, featureSet))
      }
    ) {
      is HandshakeMessage.ClientInitReject ->
        throw HandshakeException.InitException(response.errorString ?: "Unknown Error")
      is HandshakeMessage.ClientInitAck -> {
        channel!!.negotiatedFeatures = response.featureSet
        if (response.coreConfigured == null) {
          throw HandshakeException.InitException("Unknown Error")
        } else if (response.coreConfigured == true) {
          return CoreState.Configured
        } else {
          return CoreState.Unconfigured(
            response.backendInfo,
            response.authenticatorInfo
          )
        }
      }
      else -> throw HandshakeException.InitException("Unknown Error")
    }
  }

  override suspend fun login(username: String, password: String) {
    when (
      val response = messageQueue.wait(
        HandshakeMessage.ClientLoginAck::class.java,
        HandshakeMessage.ClientLoginReject::class.java
      ) {
        emit(HandshakeMessage.ClientLogin(username, password))
      }
    ) {
      is HandshakeMessage.ClientLoginReject ->
        throw HandshakeException.LoginException(response.errorString ?: "Unknown Error")
      is HandshakeMessage.ClientLoginAck ->
        return
      else -> throw HandshakeException.LoginException("Unknown Error")
    }
  }

  override suspend fun configureCore(
    adminUsername: String,
    adminPassword: String,
    backend: String,
    backendConfiguration: QVariantMap,
    authenticator: String,
    authenticatorConfiguration: QVariantMap
  ) {
    when (
      val response = messageQueue.wait(
        HandshakeMessage.CoreSetupAck::class.java,
        HandshakeMessage.CoreSetupReject::class.java
      ) {
        emit(
          HandshakeMessage.CoreSetupData(
            adminUsername, adminPassword, backend, backendConfiguration, authenticator, authenticatorConfiguration
          )
        )
      }
    ) {
      is HandshakeMessage.CoreSetupReject ->
        throw HandshakeException.SetupException(response.errorString ?: "Unknown Error")
      is HandshakeMessage.CoreSetupAck ->
        return
      else -> throw HandshakeException.SetupException("Unknown Error")
    }
  }

  companion object {
    private val logger = LoggerFactory.getLogger(ClientHandshakeHandler::class.java)
  }
}
