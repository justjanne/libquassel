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
import java.nio.ByteBuffer

/**
 * Serializer for [Byte]
 */
object ByteSerializer : PrimitiveSerializer<Byte> {
  override val javaType: Class<Byte> = Byte::class.javaObjectType

  override fun serialize(buffer: ChainedByteBuffer, data: Byte, featureSet: FeatureSet) {
    buffer.put(data)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): Byte {
    return buffer.get()
  }
}
