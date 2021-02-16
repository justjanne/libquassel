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
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.serializers.qt.QVariantListSerializer
import de.justjanne.libquassel.protocol.serializers.signalproxy.HeartBeatReplySerializer
import de.justjanne.libquassel.protocol.serializers.signalproxy.HeartBeatSerializer
import de.justjanne.libquassel.protocol.serializers.signalproxy.InitDataSerializer
import de.justjanne.libquassel.protocol.serializers.signalproxy.InitRequestSerializer
import de.justjanne.libquassel.protocol.serializers.signalproxy.RpcSerializer
import de.justjanne.libquassel.protocol.serializers.signalproxy.SyncSerializer
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import java.nio.ByteBuffer

/**
 * Singleton object containing all serializers for handshake message types
 */
object SignalProxyMessageSerializer : PrimitiveSerializer<SignalProxyMessage> {
  override val javaType: Class<out SignalProxyMessage> = SignalProxyMessage::class.java

  private fun serializeToList(data: SignalProxyMessage): QVariantList =
    when (data) {
      is SignalProxyMessage.Sync ->
        SyncSerializer.serialize(data)
      is SignalProxyMessage.Rpc ->
        RpcSerializer.serialize(data)
      is SignalProxyMessage.InitRequest ->
        InitRequestSerializer.serialize(data)
      is SignalProxyMessage.InitData ->
        InitDataSerializer.serialize(data)
      is SignalProxyMessage.HeartBeat ->
        HeartBeatSerializer.serialize(data)
      is SignalProxyMessage.HeartBeatReply ->
        HeartBeatReplySerializer.serialize(data)
    }

  private fun deserializeFromList(data: QVariantList): SignalProxyMessage =
    when (val type = data.firstOrNull().into<Int>(0)) {
      SyncSerializer.type ->
        SyncSerializer.deserialize(data)
      RpcSerializer.type ->
        RpcSerializer.deserialize(data)
      InitRequestSerializer.type ->
        InitRequestSerializer.deserialize(data)
      InitDataSerializer.type ->
        InitDataSerializer.deserialize(data)
      HeartBeatSerializer.type ->
        HeartBeatSerializer.deserialize(data)
      HeartBeatReplySerializer.type ->
        HeartBeatReplySerializer.deserialize(data)
      else ->
        throw NoSerializerForTypeException.SignalProxy(type)
    }

  override fun serialize(buffer: ChainedByteBuffer, data: SignalProxyMessage, featureSet: FeatureSet) {
    QVariantListSerializer.serialize(buffer, serializeToList(data), featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): SignalProxyMessage {
    return deserializeFromList(QVariantListSerializer.deserialize(buffer, featureSet))
  }
}
