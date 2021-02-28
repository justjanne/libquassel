/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
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
class UShortSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      UShortSerializer,
      QtType.UShort.serializer<UShort>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    UShortSerializer,
    0.toUShort(),
    byteBufferOf(0, 0)
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    UShortSerializer,
    UShort.MIN_VALUE,
    byteBufferOf(0, 0)
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    UShortSerializer,
    UShort.MAX_VALUE,
    byteBufferOf(255u, 255u)
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    UShortSerializer,
    0.toUShort().inv(),
    byteBufferOf(255u, 255u)
  )
}
