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
package de.justjanne.libquassel.protocol.testutil

import de.justjanne.libquassel.protocol.serializers.HandshakeSerializer
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals

fun <T> testHandshakeSerializerDirect(
  serializer: HandshakeSerializer<T>,
  data: T,
  matcher: Matcher<T>? = null
) {
  val after = serializer.deserialize(serializer.serialize(data))
  if (matcher != null) {
    assertThat(after, matcher)
  } else {
    assertEquals(data, after)
  }
}
