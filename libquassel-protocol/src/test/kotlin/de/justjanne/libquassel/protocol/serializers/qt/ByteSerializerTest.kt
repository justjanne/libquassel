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
import kotlin.experimental.inv

class ByteSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      ByteSerializer,
      QtSerializers.find<Byte>(QtType.Char),
    )
  }

  @Test
  fun testZero() = qtSerializerTest(
    ByteSerializer,
    0.toByte(),
    byteBufferOf(0)
  )

  @Test
  fun testMinimal() = qtSerializerTest(
    ByteSerializer,
    Byte.MIN_VALUE,
    byteBufferOf(-128)
  )

  @Test
  fun testMaximal() = qtSerializerTest(
    ByteSerializer,
    Byte.MAX_VALUE,
    byteBufferOf(127)
  )

  @Test
  fun testAllOnes() = qtSerializerTest(
    ByteSerializer,
    0.toByte().inv(),
    byteBufferOf(-1)
  )
}
