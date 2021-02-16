/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2020 Janne Mareike Koschinski
 * Copyright (c) 2020 The Quassel Project
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

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.DccIpDetectionMode
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [DccIpDetectionMode]
 */
object DccIpDetectionModeSerializer : PrimitiveSerializer<DccIpDetectionMode?> {

  override val javaType: Class<out DccIpDetectionMode?> =
    DccIpDetectionMode::class.java

  override fun serialize(
    buffer: ChainedByteBuffer,
    data: DccIpDetectionMode?,
    featureSet: FeatureSet
  ) {
    UByteSerializer.serialize(buffer, data?.value ?: 0x00u, featureSet)
  }

  override fun deserialize(
    buffer: ByteBuffer,
    featureSet: FeatureSet
  ): DccIpDetectionMode? {
    return DccIpDetectionMode.of(UByteSerializer.deserialize(buffer, featureSet))
  }
}
