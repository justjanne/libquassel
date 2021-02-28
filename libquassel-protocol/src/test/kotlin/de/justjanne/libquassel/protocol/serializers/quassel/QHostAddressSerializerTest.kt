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

import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

@Tag("QuasselSerializerTest")
class QHostAddressSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QHostAddressSerializer,
      QuasselType.QHostAddress.serializer<InetAddress>(),
    )
  }

  @Test
  fun testIpv4() = primitiveSerializerTest(
    QHostAddressSerializer,
    Inet4Address.getByAddress(
      byteArrayOf(
        127, 0, 0, 1
      )
    ),
    byteBufferOf(
      0x00,
      127, 0, 0, 1
    )
  )

  @Test
  fun testIpv6() = primitiveSerializerTest(
    QHostAddressSerializer,
    Inet6Address.getByAddress(
      ubyteArrayOf(
        0x26u, 0x07u, 0xf1u, 0x88u, 0x00u, 0x00u, 0x00u, 0x00u, 0xdeu, 0xadu, 0xbeu, 0xefu, 0xcau, 0xfeu, 0xfeu, 0xd1u,
      ).toByteArray()
    ),
    byteBufferOf(
      0x01u,
      0x26u, 0x07u, 0xf1u, 0x88u, 0x00u, 0x00u, 0x00u, 0x00u, 0xdeu, 0xadu, 0xbeu, 0xefu, 0xcau, 0xfeu, 0xfeu, 0xd1u,
    )
  )
}
