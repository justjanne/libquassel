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

import de.justjanne.libquassel.protocol.serializers.QtSerializers
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.matchers.ByteBufferMatcher
import de.justjanne.libquassel.protocol.testutil.qtSerializerTest
import de.justjanne.libquassel.protocol.variant.QtType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

class ByteBufferSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      ByteBufferSerializer,
      QtSerializers.find<ByteBuffer>(QtType.QByteArray),
    )
  }

  @Test
  fun testBaseCase() = qtSerializerTest(
    ByteBufferSerializer,
    byteBufferOf(0),
    byteBufferOf(0, 0, 0, 1, 0),
    ::ByteBufferMatcher
  )

  @Test
  fun testNormal() = qtSerializerTest(
    ByteBufferSerializer,
    byteBufferOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
    byteBufferOf(0, 0, 0, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9),
    ::ByteBufferMatcher
  )

  @Test
  fun testEmpty() = qtSerializerTest(
    ByteBufferSerializer,
    ByteBuffer.allocate(0),
    byteBufferOf(0, 0, 0, 0),
    ::ByteBufferMatcher
  )

  @Test
  fun testNull() {
    qtSerializerTest(
      ByteBufferSerializer,
      null,
      byteBufferOf(0, 0, 0, 0),
      ::ByteBufferMatcher,
      serializeFeatureSet = null
    )

    qtSerializerTest(
      ByteBufferSerializer,
      null,
      byteBufferOf(-1, -1, -1, -1),
      ::ByteBufferMatcher
    )
  }
}
