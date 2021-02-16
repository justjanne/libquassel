/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.serializers.handshake.ClientInitAckSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.ClientInitRejectSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.ClientInitSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.ClientLoginAckSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.ClientLoginRejectSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.ClientLoginSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.CoreSetupAckSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.CoreSetupDataSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.CoreSetupRejectSerializer
import de.justjanne.libquassel.protocol.serializers.handshake.SessionInitSerializer
import de.justjanne.libquassel.protocol.serializers.qt.HandshakeMapSerializer
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import java.nio.ByteBuffer

/**
 * Singleton object containing all serializers for handshake message types
 */
object HandshakeMessageSerializer : PrimitiveSerializer<HandshakeMessage> {
  override val javaType: Class<out HandshakeMessage> = HandshakeMessage::class.java

  private fun serializeToMap(data: HandshakeMessage): QVariantMap =
    when (data) {
      is HandshakeMessage.ClientInit ->
        ClientInitSerializer.serialize(data)
      is HandshakeMessage.ClientInitAck ->
        ClientInitAckSerializer.serialize(data)
      is HandshakeMessage.ClientInitReject ->
        ClientInitRejectSerializer.serialize(data)
      is HandshakeMessage.ClientLogin ->
        ClientLoginSerializer.serialize(data)
      is HandshakeMessage.ClientLoginAck ->
        ClientLoginAckSerializer.serialize(data)
      is HandshakeMessage.ClientLoginReject ->
        ClientLoginRejectSerializer.serialize(data)
      is HandshakeMessage.CoreSetupAck ->
        CoreSetupAckSerializer.serialize(data)
      is HandshakeMessage.CoreSetupData ->
        CoreSetupDataSerializer.serialize(data)
      is HandshakeMessage.CoreSetupReject ->
        CoreSetupRejectSerializer.serialize(data)
      is HandshakeMessage.SessionInit ->
        SessionInitSerializer.serialize(data)
    }

  private fun deserializeFromMap(data: QVariantMap): HandshakeMessage =
    when (val type = data["MsgType"].into<String>("")) {
      ClientInitSerializer.type ->
        ClientInitSerializer.deserialize(data)
      ClientInitAckSerializer.type ->
        ClientInitAckSerializer.deserialize(data)
      ClientInitRejectSerializer.type ->
        ClientInitRejectSerializer.deserialize(data)
      ClientLoginSerializer.type ->
        ClientLoginSerializer.deserialize(data)
      ClientLoginAckSerializer.type ->
        ClientLoginAckSerializer.deserialize(data)
      ClientLoginRejectSerializer.type ->
        ClientLoginRejectSerializer.deserialize(data)
      CoreSetupAckSerializer.type ->
        CoreSetupAckSerializer.deserialize(data)
      CoreSetupDataSerializer.type ->
        CoreSetupDataSerializer.deserialize(data)
      CoreSetupRejectSerializer.type ->
        CoreSetupRejectSerializer.deserialize(data)
      SessionInitSerializer.type ->
        SessionInitSerializer.deserialize(data)
      else ->
        throw NoSerializerForTypeException.Handshake(type)
    }

  override fun serialize(buffer: ChainedByteBuffer, data: HandshakeMessage, featureSet: FeatureSet) {
    HandshakeMapSerializer.serialize(buffer, serializeToMap(data), featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): HandshakeMessage {
    return deserializeFromMap(HandshakeMapSerializer.deserialize(buffer, featureSet))
  }
}
