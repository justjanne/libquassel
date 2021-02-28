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
import de.justjanne.libquassel.protocol.models.dcc.TransferStatus
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [TransferStatus]
 */
object TransferStatusSerializer : PrimitiveSerializer<TransferStatus?> {
  override val javaType: Class<out TransferStatus?> = TransferStatus::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: TransferStatus?, featureSet: FeatureSet) {
    IntSerializer.serialize(buffer, data?.value ?: 0, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet) = TransferStatus.of(
    IntSerializer.deserialize(buffer, featureSet)
  )
}
