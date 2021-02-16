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

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.features.QuasselFeature
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.LongSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [MsgId]
 */
object MsgIdSerializer : PrimitiveSerializer<MsgId> {
  override val javaType: Class<out MsgId> = MsgId::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: MsgId, featureSet: FeatureSet) {
    if (featureSet.hasFeature(QuasselFeature.LongMessageId)) {
      LongSerializer.serialize(buffer, data.id, featureSet)
    } else {
      IntSerializer.serialize(buffer, data.id.toInt(), featureSet)
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): MsgId {
    return if (featureSet.hasFeature(QuasselFeature.LongMessageId)) {
      MsgId(LongSerializer.deserialize(buffer, featureSet))
    } else {
      MsgId(IntSerializer.deserialize(buffer, featureSet).toLong())
    }
  }
}
