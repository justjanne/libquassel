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
class IntSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      IntSerializer,
      QtType.Int.serializer<Int>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    IntSerializer,
    0,
    byteBufferOf(0, 0, 0, 0)
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    IntSerializer,
    Int.MIN_VALUE,
    byteBufferOf(-128, 0, 0, 0)
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    IntSerializer,
    Int.MAX_VALUE,
    byteBufferOf(127, -1, -1, -1)
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    IntSerializer,
    0.inv(),
    byteBufferOf(-1, -1, -1, -1)
  )
}
