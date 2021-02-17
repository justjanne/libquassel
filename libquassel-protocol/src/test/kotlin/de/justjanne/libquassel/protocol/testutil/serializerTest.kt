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

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.useChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.testutil.matchers.ByteBufferMatcher
import de.justjanne.libquassel.protocol.util.withRewind
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import java.nio.ByteBuffer

fun <T : Any?> serializerTest(
  serializer: PrimitiveSerializer<T>,
  value: T,
  encoded: ByteBuffer? = null,
  matcher: ((T) -> Matcher<T>)? = null,
  deserializeFeatureSet: FeatureSet? = FeatureSet.all(),
  serializeFeatureSet: FeatureSet? = FeatureSet.all(),
) {
  if (encoded != null) {
    if (deserializeFeatureSet != null) {
      val after = serializer.deserialize(encoded.withRewind(), deserializeFeatureSet)
      assertEquals(0, encoded.remaining())
      if (matcher != null) {
        assertThat(after, matcher(value))
      } else {
        assertEquals(value, after)
      }
    }
    if (serializeFeatureSet != null) {
      val after = useChainedByteBuffer {
        serializer.serialize(it, value, serializeFeatureSet)
      }
      assertThat(after, ByteBufferMatcher(encoded.withRewind()))
    }
  }
}
