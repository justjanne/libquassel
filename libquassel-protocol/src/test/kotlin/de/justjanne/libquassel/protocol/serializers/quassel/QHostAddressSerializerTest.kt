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

import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.quasselSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

class QHostAddressSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QHostAddressSerializer,
      QuasselType.QHostAddress.serializer<InetAddress>(),
    )
  }

  @Test
  fun testIpv4() = quasselSerializerTest(
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
  fun testIpv6() = quasselSerializerTest(
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
