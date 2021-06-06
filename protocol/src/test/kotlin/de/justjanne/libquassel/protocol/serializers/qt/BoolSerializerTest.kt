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
class BoolSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      BoolSerializer,
      QtType.Bool.serializer<Boolean>(),
    )
  }

  @Test
  fun testTrue() = primitiveSerializerTest(
    BoolSerializer,
    true,
    byteBufferOf(1)
  )

  @Test
  fun testFalse() = primitiveSerializerTest(
    BoolSerializer,
    false,
    byteBufferOf(0)
  )
}
