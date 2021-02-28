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
