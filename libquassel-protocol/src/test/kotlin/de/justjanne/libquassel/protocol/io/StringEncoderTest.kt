/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.matchers.ByteBufferMatcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

class StringEncoderTest {
  private val ascii = StringEncoder(Charsets.ISO_8859_1)
  private val utf8 = StringEncoder(Charsets.UTF_8)
  private val utf16 = StringEncoder(Charsets.UTF_16BE)

  @Test
  fun testNullString() {
    assertThat(
      ascii.encode(null),
      ByteBufferMatcher(ByteBuffer.allocate(0))
    )
    assertThat(
      utf8.encode(null),
      ByteBufferMatcher(ByteBuffer.allocate(0))
    )
    assertThat(
      utf16.encode(null),
      ByteBufferMatcher(ByteBuffer.allocate(0))
    )
  }

  @Test
  fun testUnencodableString() {
    assertEquals(
      0,
      ascii.encode("\uFFFF").remaining()
    )
    assertThat(
      ascii.encode("\uFFFF"),
      ByteBufferMatcher(byteBufferOf())
    )
  }

  @Test
  fun testNullChar() {
    assertEquals(
      1,
      ascii.encodeChar(null).remaining()
    )
    assertThat(
      ascii.encodeChar(null),
      ByteBufferMatcher(byteBufferOf(0))
    )

    assertThat(
      utf8.encodeChar(null),
      ByteBufferMatcher(byteBufferOf(0xEFu, 0xBFu, 0xBDu))
    )

    assertEquals(
      2,
      utf16.encodeChar(null).remaining()
    )
    assertThat(
      utf16.encodeChar(null),
      ByteBufferMatcher(byteBufferOf(0xFFu, 0xFDu)),
    )
  }

  @Test
  fun testUnencodableChar() {
    assertEquals(
      1,
      ascii.encodeChar('\uFFFF').remaining()
    )
    assertThat(
      ascii.encodeChar('\uFFFF'),
      ByteBufferMatcher(byteBufferOf(0))
    )
  }
}
