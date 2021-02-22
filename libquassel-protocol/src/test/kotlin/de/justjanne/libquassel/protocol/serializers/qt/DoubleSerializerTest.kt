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
package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.serializers.QtSerializers
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.qtSerializerTest
import de.justjanne.libquassel.protocol.variant.QtType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DoubleSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      DoubleSerializer,
      QtSerializers.find<Double>(QtType.Double),
    )
  }

  @Test
  fun testZero() = qtSerializerTest(
    DoubleSerializer,
    0.0,
    byteBufferOf(0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u)
  )

  @Test
  fun testMinimal() = qtSerializerTest(
    DoubleSerializer,
    Double.MIN_VALUE,
    byteBufferOf(0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x01u)
  )

  @Test
  fun testMaximal() = qtSerializerTest(
    DoubleSerializer,
    Double.MAX_VALUE,
    byteBufferOf(0x7Fu, 0xEFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu)
  )

  @Test
  fun testInfinityPositive() = qtSerializerTest(
    DoubleSerializer,
    Double.POSITIVE_INFINITY,
    byteBufferOf(0x7Fu, 0xF0u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u)
  )

  @Test
  fun testInfinityNegative() = qtSerializerTest(
    DoubleSerializer,
    Double.NEGATIVE_INFINITY,
    byteBufferOf(0xFFu, 0xF0u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u)
  )

  @Test
  fun testNotANumber() = qtSerializerTest(
    DoubleSerializer,
    Double.NaN,
    byteBufferOf(0x7Fu, 0xF8u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u)
  )
}
