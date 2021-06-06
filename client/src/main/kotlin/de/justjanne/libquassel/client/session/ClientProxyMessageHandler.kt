/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.session

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.exceptions.RpcInvocationFailedException
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.serializers.SignalProxyMessageSerializer
import de.justjanne.libquassel.protocol.session.ProxyMessageHandler
import de.justjanne.libquassel.protocol.syncables.HeartBeatHandler
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.common.RpcHandler
import de.justjanne.libquassel.protocol.syncables.invoker.Invokers
import de.justjanne.libquassel.protocol.util.log.trace
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

class ClientProxyMessageHandler(
  private val heartBeatHandler: HeartBeatHandler,
  private val objectRepository: ObjectRepository,
  private val rpcHandler: RpcHandler
) : ProxyMessageHandler, ClientConnectionHandler() {

  override suspend fun read(buffer: ByteBuffer): Boolean {
    dispatch(SignalProxyMessageSerializer.deserialize(buffer, channel!!.negotiatedFeatures))
    return false
  }

  override suspend fun dispatch(message: SignalProxyMessage) {
    logger.trace { "Read signal proxy message $message" }
    when (message) {
      is SignalProxyMessage.HeartBeat -> emit(SignalProxyMessage.HeartBeatReply(message.timestamp))
      is SignalProxyMessage.HeartBeatReply -> heartBeatHandler.recomputeLatency(message.timestamp, force = true)
      is SignalProxyMessage.InitData -> objectRepository.init(
        objectRepository.find(message.className, message.objectName) ?: return,
        message.initData
      )
      is SignalProxyMessage.InitRequest -> {
        // Ignore incoming requests, we’re a client, we shouldn’t ever receive these
      }
      is SignalProxyMessage.Rpc -> {
        val invoker = Invokers.get(ProtocolSide.CLIENT, "RpcHandler")
          ?: throw RpcInvocationFailedException.InvokerNotFoundException("RpcHandler")
        invoker.invoke(rpcHandler, message.slotName, message.params)
      }
      is SignalProxyMessage.Sync -> {
        val invoker = Invokers.get(ProtocolSide.CLIENT, message.className)
          ?: throw RpcInvocationFailedException.InvokerNotFoundException(message.className)
        val syncable = objectRepository.find(message.className, message.objectName)
          ?: throw RpcInvocationFailedException.SyncableNotFoundException(message.className, message.objectName)
        invoker.invoke(syncable, message.slotName, message.params)
      }
    }
  }

  companion object {
    private val logger = LoggerFactory.getLogger(ClientProxyMessageHandler::class.java)
  }
}
