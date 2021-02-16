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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

/**
 * Serializer for [QVariantMap] during handshake,
 * which uses a special format instead of [QVariantMapSerializer]
 */
object HandshakeMapSerializer : PrimitiveSerializer<QVariantMap> {

  @Suppress("UNCHECKED_CAST")
  override val javaType: Class<out QVariantMap> = Map::class.java as Class<QVariantMap>

  override fun serialize(buffer: ChainedByteBuffer, data: QVariantMap, featureSet: FeatureSet) {
    val list: QVariantList = data.entries.flatMap { (key, value) ->
      val encodedKey = StringSerializerUtf8.serializeRaw(key)
      listOf(qVariant(encodedKey, QtType.QByteArray), value)
    }

    QVariantListSerializer.serialize(buffer, list, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): QVariantMap {
    val list = QVariantListSerializer.deserialize(buffer, featureSet)
    return (list.indices step 2).map {
      val encodedKey = list[it].into<ByteBuffer>(ByteBuffer.allocateDirect(0))
      val value = list[it + 1]

      Pair(StringSerializerUtf8.deserializeRaw(encodedKey), value)
    }.toMap()
  }
}
