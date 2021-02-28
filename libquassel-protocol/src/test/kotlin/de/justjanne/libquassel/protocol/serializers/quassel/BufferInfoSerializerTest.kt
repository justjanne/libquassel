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

import de.justjanne.bitflags.none
import de.justjanne.bitflags.validValues
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QuasselSerializerTest")
class BufferInfoSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      BufferInfoSerializer,
      QuasselType.BufferInfo.serializer<BufferInfo>(),
    )
  }

  @Test
  fun testBaseCase() = primitiveSerializerTest(
    BufferInfoSerializer,
    BufferInfo(
      BufferId(-1),
      NetworkId(-1),
      BufferType.none(),
      -1,
      ""
    ),
    byteBufferOf(
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0x00u,
      0x00u,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0x00u,
      0x00u,
      0x00u,
      0x00u
    )
  )

  @Test
  fun testNormal() = primitiveSerializerTest(
    BufferInfoSerializer,
    BufferInfo(
      BufferId.MAX_VALUE,
      NetworkId.MAX_VALUE,
      BufferType.validValues(),
      Int.MAX_VALUE,
      "äẞ\u0000\uFFFF"
    ),
    byteBufferOf(
      127,
      -1,
      -1,
      -1,
      127,
      -1,
      -1,
      -1,
      0,
      15,
      127,
      -1,
      -1,
      -1,
      0,
      0,
      0,
      9,
      -61,
      -92,
      -31,
      -70,
      -98,
      0,
      -17,
      -65,
      -65
    )
  )
}
