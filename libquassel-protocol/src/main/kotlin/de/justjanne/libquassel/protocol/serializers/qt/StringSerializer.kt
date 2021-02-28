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
import de.justjanne.libquassel.protocol.io.StringEncoder
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import java.nio.ByteBuffer
import java.nio.charset.Charset
import kotlin.concurrent.getOrSet

/**
 * Abstract serializer for Strings.
 *
 * For concrete implementations see [StringSerializerAscii], [StringSerializerUtf8] and [StringSerializerUtf16]
 */
abstract class StringSerializer(
  private val charset: Charset,
  private val nullLimited: Boolean,
) : PrimitiveSerializer<String?> {
  override val javaType: Class<out String> = String::class.java

  private val encoderLocal = ThreadLocal<StringEncoder>()
  private fun encoder() = encoderLocal.getOrSet { StringEncoder(charset) }

  private fun addNullBytes(before: Int) = if (nullLimited) before + 1 else before
  private fun removeNullBytes(before: Int) = if (nullLimited) before - 1 else before

  override fun serialize(buffer: ChainedByteBuffer, data: String?, featureSet: FeatureSet) {
    if (data == null) {
      IntSerializer.serialize(buffer, -1, featureSet)
    } else {
      val encodedData = encoder().encode(data)
      IntSerializer.serialize(buffer, addNullBytes(encodedData.remaining()), featureSet)
      buffer.put(encodedData)
      if (nullLimited) {
        buffer.put(0)
      }
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): String? {
    val length = IntSerializer.deserialize(buffer, featureSet)
    if (length < 0) {
      return null
    }
    val result = encoder().decode(buffer, removeNullBytes(length))
    if (nullLimited) {
      buffer.position(addNullBytes(buffer.position()))
    }
    return result
  }

  /**
   * Serialize a string, without length prefix, as a byte size
   */
  fun serializeRaw(data: String?): ByteBuffer {
    val result = encoder().encode(data)
    if (nullLimited) {
      val buffer = ByteBuffer.allocateDirect(result.remaining() + 1)
      buffer.put(result)
      buffer.clear()
      return buffer
    }
    return result
  }

  /**
   * Deserialize a string from a given byte slice
   */
  fun deserializeRaw(data: ByteBuffer?): String {
    if (data == null) {
      return ""
    }
    if (nullLimited) {
      data.limit(removeNullBytes(data.limit()))
    }
    return encoder().decode(data)
  }
}
