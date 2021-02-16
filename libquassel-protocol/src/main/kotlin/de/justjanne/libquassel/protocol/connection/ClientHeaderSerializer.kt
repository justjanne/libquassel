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
import de.justjanne.libquassel.protocol.serializers.qt.UIntSerializer
import java.nio.ByteBuffer

/**
 * Serializer for a [ClientHeader]
 */
object ClientHeaderSerializer : PrimitiveSerializer<ClientHeader> {
  override val javaType: Class<out ClientHeader> = ClientHeader::class.java
  private const val magic: UInt = 0x42b3_3f00u
  private const val featureMask: UInt = 0x0000_00ffu
  private const val lastMagic: UByte = 0x80u

  private fun addMagic(data: UByte): UInt =
    magic or data.toUInt()

  private fun removeMagic(data: UInt): UByte =
    (data and featureMask).toUByte()

  private fun <T> writeList(
    buffer: ChainedByteBuffer,
    list: List<T>,
    featureSet: FeatureSet,
    f: (T) -> Unit
  ) {
    for (index in list.indices) {
      val isLast = index + 1 == list.size
      val magic = if (isLast) lastMagic else 0x00u
      UByteSerializer.serialize(buffer, magic, featureSet)
      f(list[index])
    }
  }

  private fun <T> readList(
    buffer: ByteBuffer,
    featureSet: FeatureSet,
    f: () -> T
  ): List<T> {
    val list = mutableListOf<T>()
    while (true) {
      val isLast = UByteSerializer.deserialize(buffer, featureSet) != 0x00u.toUByte()
      list.add(f())
      if (isLast) {
        break
      }
    }
    return list
  }

  override fun serialize(buffer: ChainedByteBuffer, data: ClientHeader, featureSet: FeatureSet) {
    UIntSerializer.serialize(buffer, addMagic(data.features.toBits()), featureSet)
    writeList(buffer, data.versions, featureSet) {
      ProtocolMetaSerializer.serialize(buffer, it, featureSet)
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet) = ClientHeader(
    features = ProtocolFeature.of(
      removeMagic(UIntSerializer.deserialize(buffer, featureSet))
    ),
    versions = readList(buffer, featureSet) {
      ProtocolMetaSerializer.deserialize(buffer, featureSet)
    }
  )
}
