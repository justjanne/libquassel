package de.justjanne.libquassel.protocol.serializers.signalproxy

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.SignalProxySerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

/**
 * Serializer for [SignalProxyMessage.Rpc]
 */
object RpcSerializer : SignalProxySerializer<SignalProxyMessage.Rpc> {
  override val type: Int = 2

  override fun serialize(data: SignalProxyMessage.Rpc) = listOf(
    qVariant(SyncSerializer.type, QtType.Int),
    qVariant(StringSerializerUtf8.serializeRaw(data.slotName), QtType.QByteArray)
  ) + data.params

  override fun deserialize(data: QVariantList) = SignalProxyMessage.Rpc(
    StringSerializerUtf8.deserializeRaw(data[1].into<ByteBuffer>()),
    data.drop(2)
  )
}
