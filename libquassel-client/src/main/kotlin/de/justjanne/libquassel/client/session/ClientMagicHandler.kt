/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.session

import de.justjanne.libquassel.protocol.connection.ClientHeader
import de.justjanne.libquassel.protocol.connection.ClientHeaderSerializer
import de.justjanne.libquassel.protocol.connection.CoreHeaderSerializer
import de.justjanne.libquassel.protocol.connection.ProtocolFeature
import de.justjanne.libquassel.protocol.connection.ProtocolFeatures
import de.justjanne.libquassel.protocol.connection.ProtocolMeta
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.session.ConnectionHandler
import de.justjanne.libquassel.protocol.session.MessageChannel
import de.justjanne.libquassel.protocol.util.log.trace
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import javax.net.ssl.SSLContext

class ClientMagicHandler(
  private val protocolFeatures: ProtocolFeatures,
  private val protocols: List<ProtocolMeta>,
  private val sslContext: SSLContext
) : ConnectionHandler {
  private val connectionFeatureSet = FeatureSet.none()

  override suspend fun init(channel: MessageChannel): Boolean {
    val header = ClientHeader(
      features = protocolFeatures,
      versions = protocols
    )
    logger.trace { "Writing client header $header" }
    channel.emit(sizePrefix = false) {
      ClientHeaderSerializer.serialize(
        it,
        header,
        connectionFeatureSet
      )
    }

    val handshakeBuffer = ByteBuffer.allocateDirect(4)
    channel.channel.read(handshakeBuffer)
    handshakeBuffer.flip()
    val protocol = CoreHeaderSerializer.deserialize(handshakeBuffer, connectionFeatureSet)
    logger.trace { "Read server header $protocol" }
    if (protocol.features.contains(ProtocolFeature.TLS)) {
      channel.channel.enableTLS(sslContext)
    }
    if (protocol.features.contains(ProtocolFeature.Compression)) {
      channel.channel.enableCompression()
    }
    return true
  }

  override suspend fun read(buffer: ByteBuffer) = true

  override suspend fun done() = Unit

  companion object {
    private val logger = LoggerFactory.getLogger(ClientMagicHandler::class.java)
  }
}
