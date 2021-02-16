package de.justjanne.libquassel.protocol.serializers.signalproxy

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.SignalProxySerializer
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import org.threeten.bp.Instant

/**
 * Serializer for [SignalProxyMessage.HeartBeat]
 */
object HeartBeatSerializer : SignalProxySerializer<SignalProxyMessage.HeartBeat> {
  override val type: Int = 5

  override fun serialize(data: SignalProxyMessage.HeartBeat) = listOf(
    qVariant(type, QtType.Int),
    qVariant(data.timestamp, QtType.QDateTime)
  )

  override fun deserialize(data: QVariantList) = SignalProxyMessage.HeartBeat(
    data[1].into(Instant.EPOCH)
  )
}
