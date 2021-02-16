/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.features.QuasselFeature
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.Message
import de.justjanne.libquassel.protocol.models.flags.MessageFlag
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.LongSerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UIntSerializer
import org.threeten.bp.Instant
import java.nio.ByteBuffer

/**
 * Serializer for [Message]
 */
object MessageSerializer : PrimitiveSerializer<Message> {
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
