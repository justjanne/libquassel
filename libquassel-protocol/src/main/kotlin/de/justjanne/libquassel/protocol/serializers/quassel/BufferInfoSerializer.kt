/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.serializers.qt.UShortSerializer
import java.nio.ByteBuffer

/**
 * Serializer for [BufferInfo]
 */
object BufferInfoSerializer : PrimitiveSerializer<BufferInfo> {
  override val javaType: Class<out BufferInfo> = BufferInfo::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: BufferInfo, featureSet: FeatureSet) {
    BufferIdSerializer.serialize(buffer, data.bufferId, featureSet)
    NetworkIdSerializer.serialize(buffer, data.networkId, featureSet)
    UShortSerializer.serialize(buffer, data.type.toBits(), featureSet)
    IntSerializer.serialize(buffer, data.groupId, featureSet)
    StringSerializerUtf8.serialize(buffer, data.bufferName, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): BufferInfo {
    val bufferId = BufferIdSerializer.deserialize(buffer, featureSet)
    val networkId = NetworkIdSerializer.deserialize(buffer, featureSet)
    val type = BufferType.of(UShortSerializer.deserialize(buffer, featureSet))
    val groupId = IntSerializer.deserialize(buffer, featureSet)
    val bufferName = StringSerializerUtf8.deserialize(buffer, featureSet)
    return BufferInfo(
      bufferId = bufferId,
      networkId = networkId,
      type = type,
      groupId = groupId,
      bufferName = bufferName
    )
  }
}
