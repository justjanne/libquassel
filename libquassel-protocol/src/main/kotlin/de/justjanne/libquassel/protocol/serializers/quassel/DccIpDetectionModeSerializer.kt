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
import de.justjanne.libquassel.protocol.models.dcc.DccIpDetectionMode
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
