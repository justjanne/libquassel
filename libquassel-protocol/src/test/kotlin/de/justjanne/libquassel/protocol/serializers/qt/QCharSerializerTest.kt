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
import de.justjanne.libquassel.protocol.testutil.matchers.BomMatcherChar
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QtSerializerTest")
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
