package de.justjanne.libquassel.protocol.serializers.signalproxy

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.SignalProxySerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

object InitRequestSerializer : SignalProxySerializer<SignalProxyMessage.InitRequest> {
  override val type: Int = 3

  override fun serialize(data: SignalProxyMessage.InitRequest) = listOf(
    qVariant(InitDataSerializer.type, QtType.Int),
    qVariant(StringSerializerUtf8.serializeRaw(data.className), QtType.QByteArray),
    qVariant(StringSerializerUtf8.serializeRaw(data.objectName), QtType.QByteArray),
  )

  override fun deserialize(data: QVariantList) = SignalProxyMessage.InitRequest(
    StringSerializerUtf8.deserializeRaw(data[1].into<ByteBuffer>()),
    StringSerializerUtf8.deserializeRaw(data[2].into<ByteBuffer>())
  )
}
