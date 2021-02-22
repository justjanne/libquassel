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
import de.justjanne.libquassel.protocol.testutil.matchers.BomMatcherChar
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QCharSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QCharSerializer,
      QtType.QChar.serializer<Char>(),
    )
  }

  @Test
  fun testNull() = primitiveSerializerTest(
    QCharSerializer,
    '\u0000',
    byteBufferOf(0, 0),
    ::BomMatcherChar,
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    QCharSerializer,
    '\uFFFF',
    byteBufferOf(-1, -1),
    ::BomMatcherChar,
  )

  @Test
  fun testBOM1() = primitiveSerializerTest(
    QCharSerializer,
    '\uFFFE',
    byteBufferOf(-1, -2),
    ::BomMatcherChar,
  )

  @Test
  fun testBOM2() = primitiveSerializerTest(
    QCharSerializer,
    '\uFEFF',
    byteBufferOf(-2, -1),
    ::BomMatcherChar,
  )

  @Test
  fun testAlphabet() {
    for (value in 'a'..'z') primitiveSerializerTest(
      QCharSerializer,
      value,
      byteBufferOf(0, value.toByte())
    )
    for (value in 'A'..'Z') primitiveSerializerTest(
      QCharSerializer,
      value,
      byteBufferOf(0, value.toByte())
    )
    for (value in '0'..'9') primitiveSerializerTest(
      QCharSerializer,
      value,
      byteBufferOf(0, value.toByte())
    )
  }

  @Test
  fun testAlphabetExtended() {
    for (value in listOf('ä', 'ö', 'ü', 'ß', 'æ', 'ø', 'µ')) primitiveSerializerTest(
      QCharSerializer,
      value
    )
  }
}
