/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.io.copyData
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [ByteBuffer]
 */
object ByteBufferSerializer : PrimitiveSerializer<ByteBuffer?> {
  override val javaType: Class<out ByteBuffer?> = ByteBuffer::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: ByteBuffer?, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data?.remaining() ?: -1, featureSet)
    if (data != null) {
      buffer.put(data)
    }
    data?.rewind()
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): ByteBuffer? {
    val length = IntSerializer.deserialize(buffer, featureSet)
    if (length < 0) {
      return null
    }
    val result = copyData(buffer, length)
    result.limit(length)
    return result
  }
}
