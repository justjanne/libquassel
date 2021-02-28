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
import kotlin.experimental.inv

@Tag("QtSerializerTest")
class ByteSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      ByteSerializer,
      QtType.Char.serializer<Byte>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    ByteSerializer,
    0.toByte(),
    byteBufferOf(0)
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    ByteSerializer,
    Byte.MIN_VALUE,
    byteBufferOf(-128)
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    ByteSerializer,
    Byte.MAX_VALUE,
    byteBufferOf(127)
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    ByteSerializer,
    0.toByte().inv(),
    byteBufferOf(-1)
  )
}
