/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QuasselSerializerTest")
class IdentityIdSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      IdentityIdSerializer,
      QuasselType.IdentityId.serializer<IdentityId>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    IdentityIdSerializer,
    IdentityId(0),
    byteBufferOf(0, 0, 0, 0)
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    IdentityIdSerializer,
    IdentityId.MIN_VALUE,
    byteBufferOf(-128, 0, 0, 0)
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    IdentityIdSerializer,
    IdentityId.MAX_VALUE,
    byteBufferOf(127, -1, -1, -1)
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    IdentityIdSerializer,
    IdentityId(0.inv()),
    byteBufferOf(-1, -1, -1, -1)
  )
}
