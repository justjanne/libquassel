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
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [Boolean]
 */
object BoolSerializer : PrimitiveSerializer<Boolean> {
  override val javaType: Class<Boolean> = Boolean::class.javaObjectType

  override fun serialize(buffer: ChainedByteBuffer, data: Boolean, featureSet: FeatureSet) {
    buffer.put(
      if (data) 0x01.toByte()
      else 0x00.toByte()
    )
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): Boolean {
    return buffer.get() != 0x00.toByte()
  }
}
