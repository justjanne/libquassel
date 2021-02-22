/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.features.QuasselFeature
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.QuasselSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.LongSerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UIntSerializer
import de.justjanne.libquassel.protocol.types.Message
import de.justjanne.libquassel.protocol.types.MessageFlag
import de.justjanne.libquassel.protocol.types.MessageType
import de.justjanne.libquassel.protocol.variant.QuasselType
import org.threeten.bp.Instant
import java.nio.ByteBuffer

object MessageSerializer : QuasselSerializer<Message> {
  override val quasselType: QuasselType = QuasselType.Message
  override val javaType: Class<out Message> = Message::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: Message, featureSet: FeatureSet) {
    MsgIdSerializer.serialize(buffer, data.messageId, featureSet)
    if (featureSet.hasFeature(QuasselFeature.LongTime)) {
      LongSerializer.serialize(buffer, data.time.toEpochMilli(), featureSet)
    } else {
      IntSerializer.serialize(buffer, data.time.epochSecond.toInt(), featureSet)
    }
    UIntSerializer.serialize(buffer, data.type.toBits(), featureSet)
    UByteSerializer.serialize(buffer, data.flag.toBits().toUByte(), featureSet)
    BufferInfoSerializer.serialize(buffer, data.bufferInfo, featureSet)
    StringSerializerUtf8.serialize(buffer, data.sender, featureSet)
    if (featureSet.hasFeature(QuasselFeature.SenderPrefixes)) {
      StringSerializerUtf8.serialize(buffer, data.senderPrefixes, featureSet)
    }
    if (featureSet.hasFeature(QuasselFeature.RichMessages)) {
      StringSerializerUtf8.serialize(buffer, data.realName, featureSet)
      StringSerializerUtf8.serialize(buffer, data.avatarUrl, featureSet)
    }
    StringSerializerUtf8.serialize(buffer, data.content, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): Message {
    return Message(
      messageId = MsgIdSerializer.deserialize(buffer, featureSet),
      time = if (featureSet.hasFeature(QuasselFeature.LongTime))
        Instant.ofEpochMilli(LongSerializer.deserialize(buffer, featureSet))
      else
        Instant.ofEpochSecond(IntSerializer.deserialize(buffer, featureSet).toLong()),
      type = MessageType.of(UIntSerializer.deserialize(buffer, featureSet)),
      flag = MessageFlag.of(
        UByteSerializer.deserialize(buffer, featureSet).toUInt()
      ),
      bufferInfo = BufferInfoSerializer.deserialize(buffer, featureSet),
      sender = StringSerializerUtf8.deserialize(buffer, featureSet) ?: "",
      senderPrefixes = if (featureSet.hasFeature(QuasselFeature.SenderPrefixes))
        StringSerializerUtf8.deserialize(buffer, featureSet) ?: "" else "",
      realName = if (featureSet.hasFeature(QuasselFeature.RichMessages))
        StringSerializerUtf8.deserialize(buffer, featureSet) ?: "" else "",
      avatarUrl = if (featureSet.hasFeature(QuasselFeature.RichMessages))
        StringSerializerUtf8.deserialize(buffer, featureSet) ?: "" else "",
      content = StringSerializerUtf8.deserialize(buffer, featureSet) ?: ""
    )
  }
}
