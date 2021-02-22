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
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QVariant_
import java.nio.ByteBuffer

/**
 * Serializer for [QVariantMap]
 */
object QVariantMapSerializer : PrimitiveSerializer<QVariantMap> {

  @Suppress("UNCHECKED_CAST")
  override val javaType: Class<out QVariantMap> = Map::class.java as Class<QVariantMap>

  override fun serialize(buffer: ChainedByteBuffer, data: QVariantMap, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data.size, featureSet)
    data.entries.forEach { (key, value) ->
      StringSerializerUtf16.serialize(buffer, key, featureSet)
      QVariantSerializer.serialize(buffer, value, featureSet)
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): QVariantMap {
    val result = mutableMapOf<String, QVariant_>()
    val length = IntSerializer.deserialize(buffer, featureSet)
    for (i in 0 until length) {
      val key = StringSerializerUtf16.deserialize(buffer, featureSet) ?: ""
      val value = QVariantSerializer.deserialize(buffer, featureSet)
      result[key] = value
    }
    return result
  }
}
