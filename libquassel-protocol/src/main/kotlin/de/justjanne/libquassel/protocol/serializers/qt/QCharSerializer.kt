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
