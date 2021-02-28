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
import de.justjanne.libquassel.protocol.models.dcc.TransferDirection
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [TransferDirection]
 */
object TransferDirectionSerializer : PrimitiveSerializer<TransferDirection?> {
  override val javaType: Class<out TransferDirection?> = TransferDirection::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: TransferDirection?, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data?.value ?: 0, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet) = TransferDirection.of(
    IntSerializer.deserialize(buffer, featureSet)
  )
}
