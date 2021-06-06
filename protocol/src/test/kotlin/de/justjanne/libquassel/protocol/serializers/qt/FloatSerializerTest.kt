/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QtSerializerTest")
class FloatSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      FloatSerializer,
      QtType.Float.serializer<Float>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    FloatSerializer,
    0f,
    byteBufferOf(0x00u, 0x00u, 0x00u, 0x00u)
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    FloatSerializer,
    Float.MIN_VALUE,
    byteBufferOf(0x00u, 0x00u, 0x00u, 0x01u)
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    FloatSerializer,
    Float.MAX_VALUE,
    byteBufferOf(0x7Fu, 0x7Fu, 0xFFu, 0xFFu)
  )

  @Test
  fun testInfinityPositive() = primitiveSerializerTest(
    FloatSerializer,
    Float.POSITIVE_INFINITY,
    byteBufferOf(0x7Fu, 0x80u, 0x00u, 0x00u)
  )

  @Test
  fun testInfinityNegative() = primitiveSerializerTest(
    FloatSerializer,
    Float.NEGATIVE_INFINITY,
    byteBufferOf(0xFFu, 0x80u, 0x00u, 0x00u)
  )

  @Test
  fun testNotANumber() = primitiveSerializerTest(
    FloatSerializer,
    Float.NaN,
    byteBufferOf(0x7Fu, 0xC0u, 0x00u, 0x00u)
  )
}
