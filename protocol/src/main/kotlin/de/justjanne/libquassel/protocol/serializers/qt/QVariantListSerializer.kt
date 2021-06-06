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
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariant_
import java.nio.ByteBuffer

/**
 * Serializer for [QVariantList]
 */
object QVariantListSerializer : PrimitiveSerializer<QVariantList> {

  @Suppress("UNCHECKED_CAST")
  override val javaType: Class<QVariantList> = List::class.java as Class<QVariantList>

  override fun serialize(buffer: ChainedByteBuffer, data: QVariantList, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data.size, featureSet)
    data.forEach {
      QVariantSerializer.serialize(buffer, it, featureSet)
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): QVariantList {
    val result = mutableListOf<QVariant_>()
    val length = IntSerializer.deserialize(buffer, featureSet)
    for (i in 0 until length) {
      result.add(QVariantSerializer.deserialize(buffer, featureSet))
    }
    return result
  }
}
