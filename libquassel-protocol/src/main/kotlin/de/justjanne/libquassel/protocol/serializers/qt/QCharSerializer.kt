/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
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
import kotlin.concurrent.getOrSet

/**
 * Serializer for [Char]
 */
object QCharSerializer : PrimitiveSerializer<Char> {
  override val javaType: Class<out Char> = Char::class.javaObjectType

  private val encoderLocal = ThreadLocal<StringEncoder>()
  private fun encoder() = encoderLocal.getOrSet { StringEncoder(Charsets.UTF_16BE) }

  override fun serialize(buffer: ChainedByteBuffer, data: Char, featureSet: FeatureSet) {
    buffer.put(encoder().encodeChar(data))
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): Char {
    return encoder().decodeChar(buffer)
  }
}
