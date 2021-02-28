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

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QtSerializerTest")
class UByteSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      UByteSerializer,
      QtType.UChar.serializer<UByte>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    UByteSerializer,
    0.toUByte(),
    byteBufferOf(0)
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    UByteSerializer,
    UByte.MIN_VALUE,
    byteBufferOf(0)
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    UByteSerializer,
    UByte.MAX_VALUE,
    byteBufferOf(255u)
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    UByteSerializer,
    0.toUByte().inv(),
    byteBufferOf(255u)
  )
}
