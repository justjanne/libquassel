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
