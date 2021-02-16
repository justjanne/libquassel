/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.connection

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import java.nio.ByteBuffer

/**
 * Serializer for a [CoreHeader]
 */
object CoreHeaderSerializer : PrimitiveSerializer<CoreHeader> {
  override val javaType: Class<out CoreHeader> = CoreHeader::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: CoreHeader, featureSet: FeatureSet) {
    UByteSerializer.serialize(buffer, data.features.toBits(), featureSet)
    ProtocolMetaSerializer.serialize(buffer, data.version, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet) = CoreHeader(
    ProtocolFeature.of(UByteSerializer.deserialize(buffer, featureSet)),
    ProtocolMetaSerializer.deserialize(buffer, featureSet)
  )
}
