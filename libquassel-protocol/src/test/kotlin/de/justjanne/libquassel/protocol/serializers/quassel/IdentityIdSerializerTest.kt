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
