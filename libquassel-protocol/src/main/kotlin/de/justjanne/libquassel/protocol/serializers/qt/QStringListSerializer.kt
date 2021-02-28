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
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [QStringList]
 */
object QStringListSerializer : PrimitiveSerializer<QStringList> {

  @Suppress("UNCHECKED_CAST")
  override val javaType: Class<QStringList> = List::class.java as Class<QStringList>

  override fun serialize(buffer: ChainedByteBuffer, data: QStringList, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data.size, featureSet)
    data.forEach {
      StringSerializerUtf16.serialize(buffer, it, featureSet)
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): QStringList {
    val result = mutableListOf<String?>()
    val length = IntSerializer.deserialize(buffer, featureSet)
    for (i in 0 until length) {
      result.add(StringSerializerUtf16.deserialize(buffer, featureSet))
    }
    return result
  }
}
