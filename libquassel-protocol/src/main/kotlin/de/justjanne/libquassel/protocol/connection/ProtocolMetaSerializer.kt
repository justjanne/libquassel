/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.connection

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UShortSerializer
import java.nio.ByteBuffer

/**
 * Serializer for a [ProtocolMeta]
 */
object ProtocolMetaSerializer : PrimitiveSerializer<ProtocolMeta> {
  override val javaType: Class<out ProtocolMeta> = ProtocolMeta::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: ProtocolMeta, featureSet: FeatureSet) {
    UShortSerializer.serialize(buffer, data.data, featureSet)
    UByteSerializer.serialize(buffer, data.version.value, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet) = ProtocolMeta(
    data = UShortSerializer.deserialize(buffer, featureSet),
    version = ProtocolVersion.of(UByteSerializer.deserialize(buffer, featureSet))
  )
}
