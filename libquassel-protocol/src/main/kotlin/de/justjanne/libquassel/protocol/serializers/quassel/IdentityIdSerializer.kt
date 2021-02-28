/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [IdentityId]
 */
object IdentityIdSerializer : PrimitiveSerializer<IdentityId> {
  override val javaType: Class<out IdentityId> = IdentityId::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: IdentityId, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data.id, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): IdentityId {
    return IdentityId(IntSerializer.deserialize(buffer, featureSet))
  }
}
