package de.justjanne.libquassel.protocol.serializers.signalproxy

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.SignalProxySerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

object SyncSerializer : SignalProxySerializer<SignalProxyMessage.Sync> {
  override val type: Int = 1

  override fun serialize(data: SignalProxyMessage.Sync) = listOf(
    qVariant(type, QtType.Int),
    qVariant(StringSerializerUtf8.serializeRaw(data.className), QtType.QByteArray),
    qVariant(StringSerializerUtf8.serializeRaw(data.objectName), QtType.QByteArray),
    qVariant(StringSerializerUtf8.serializeRaw(data.slotName), QtType.QByteArray)
  ) + data.params

  override fun deserialize(data: QVariantList) = SignalProxyMessage.Sync(
    StringSerializerUtf8.deserializeRaw(data[1].into<ByteBuffer>()),
    StringSerializerUtf8.deserializeRaw(data[2].into<ByteBuffer>()),
    StringSerializerUtf8.deserializeRaw(data[3].into<ByteBuffer>()),
    data.drop(4)
  )
}
