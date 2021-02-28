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
import java.util.UUID

@Tag("QtSerializerTest")
class UuidSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      UuidSerializer,
      QtType.Uuid.serializer<UUID>(),
    )
  }

  @Test
  fun testSimple() = primitiveSerializerTest(
    UuidSerializer,
    UUID.fromString("e4335bb0-ceef-4b9f-8ceb-be19b4da34fd"),
    byteBufferOf(
      0xE4u, 0x33u, 0x5Bu, 0xB0u, 0xCEu, 0xEFu, 0x4Bu, 0x9Fu, 0x8Cu, 0xEBu, 0xBEu, 0x19u, 0xB4u, 0xDAu, 0x34u, 0xFDu,
    )
  )
}
