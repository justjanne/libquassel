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
