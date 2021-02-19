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
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import java.nio.ByteBuffer
import java.util.UUID

/**
 * Serializer for [UUID]
 */
object UuidSerializer : PrimitiveSerializer<UUID> {
  override val javaType: Class<UUID> = UUID::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: UUID, featureSet: FeatureSet) {
    LongSerializer.serialize(buffer, data.mostSignificantBits, featureSet)
    LongSerializer.serialize(buffer, data.leastSignificantBits, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet) = UUID(
    LongSerializer.deserialize(buffer, featureSet),
    LongSerializer.deserialize(buffer, featureSet)
  )
}
