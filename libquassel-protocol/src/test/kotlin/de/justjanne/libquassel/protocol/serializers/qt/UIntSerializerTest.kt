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
class UIntSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      UIntSerializer,
      QtType.UInt.serializer<UInt>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    UIntSerializer,
    0.toUInt(),
    byteBufferOf(0, 0, 0, 0)
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    UIntSerializer,
    UInt.MIN_VALUE,
    byteBufferOf(0, 0, 0, 0)
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    UIntSerializer,
    UInt.MAX_VALUE,
    byteBufferOf(255u, 255u, 255u, 255u)
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    UIntSerializer,
    0.toUInt().inv(),
    byteBufferOf(255u, 255u, 255u, 255u)
  )
}
