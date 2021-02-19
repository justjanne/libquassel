/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.TransferIdList
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UuidSerializer
import java.nio.ByteBuffer
import java.util.UUID

/**
 * Serializer for [TransferIdList]
 */
object TransferIdListSerializer : PrimitiveSerializer<TransferIdList> {
  @Suppress("UNCHECKED_CAST")
  override val javaType: Class<TransferIdList> = List::class.java as Class<TransferIdList>

  override fun serialize(buffer: ChainedByteBuffer, data: TransferIdList, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data.size, featureSet)
    data.forEach {
      UuidSerializer.serialize(buffer, it, featureSet)
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): TransferIdList {
    val result = mutableListOf<UUID>()
    val length = IntSerializer.deserialize(buffer, featureSet)
    for (i in 0 until length) {
      result.add(UuidSerializer.deserialize(buffer, featureSet))
    }
    return result
  }
}
