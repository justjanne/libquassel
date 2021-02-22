/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
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

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.QuasselSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.serializers.qt.UShortSerializer
import de.justjanne.libquassel.protocol.types.BufferInfo
import de.justjanne.libquassel.protocol.types.BufferType
import de.justjanne.libquassel.protocol.variant.QuasselType
import java.nio.ByteBuffer

object BufferInfoSerializer : QuasselSerializer<BufferInfo> {
  override val quasselType: QuasselType = QuasselType.BufferInfo
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
