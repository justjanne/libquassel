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
import de.justjanne.libquassel.protocol.models.NetworkLayerProtocol
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.nio.ByteBuffer

/**
 * Serializer for [InetAddress]
 */
object QHostAddressSerializer : PrimitiveSerializer<InetAddress> {
  override val javaType: Class<out InetAddress> = InetAddress::class.java

  override fun serialize(
    buffer: ChainedByteBuffer,
    data: InetAddress,
    featureSet: FeatureSet
  ) {
    when (data) {
      is Inet4Address -> {
        UByteSerializer.serialize(
          buffer,
          NetworkLayerProtocol.IPv4Protocol.value,
          featureSet
        )
        buffer.put(data.address)
      }
      is Inet6Address -> {
        UByteSerializer.serialize(
          buffer,
          NetworkLayerProtocol.IPv6Protocol.value,
          featureSet
        )
        buffer.put(data.address)
      }
      else -> {
        UByteSerializer.serialize(
          buffer,
          NetworkLayerProtocol.UnknownNetworkLayerProtocol.value,
          featureSet
        )
        throw IllegalArgumentException("Invalid network protocol ${data.javaClass.canonicalName}")
      }
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): InetAddress {
    val type = UByteSerializer.deserialize(buffer, featureSet)
    return when (NetworkLayerProtocol.of(type)) {
      NetworkLayerProtocol.IPv4Protocol -> {
        val buf = ByteArray(4)
        buffer.get(buf)
        Inet4Address.getByAddress(buf)
      }
      NetworkLayerProtocol.IPv6Protocol -> {
        val buf = ByteArray(16)
        buffer.get(buf)
        Inet6Address.getByAddress(buf)
      }
      else -> {
        throw IllegalArgumentException("Invalid network protocol $type")
      }
    }
  }
}
