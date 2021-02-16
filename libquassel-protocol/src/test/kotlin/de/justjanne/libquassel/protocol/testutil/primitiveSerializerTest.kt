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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.testutil.matchers.ByteBufferMatcher
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import java.nio.ByteBuffer

inline fun <reified T : Any?> primitiveSerializerTest(
  type: QtType,
  value: T,
  encoded: ByteBuffer? = null,
  noinline matcher: ((T) -> Matcher<T>)? = null,
  featureSets: List<FeatureSet> = listOf(FeatureSet.none(), FeatureSet.all()),
  deserializeFeatureSet: FeatureSet? = FeatureSet.all(),
  serializeFeatureSet: FeatureSet? = FeatureSet.all(),
) = primitiveSerializerTest(
  type.serializer(),
  value,
  encoded,
  matcher,
  featureSets,
  deserializeFeatureSet,
  serializeFeatureSet
)

inline fun <reified T : Any?> primitiveSerializerTest(
  type: QuasselType,
  value: T,
  encoded: ByteBuffer? = null,
  noinline matcher: ((T) -> Matcher<T>)? = null,
  featureSets: List<FeatureSet> = listOf(FeatureSet.none(), FeatureSet.all()),
  deserializeFeatureSet: FeatureSet? = FeatureSet.all(),
  serializeFeatureSet: FeatureSet? = FeatureSet.all(),
) = primitiveSerializerTest(
  type.serializer(),
  value,
  encoded,
  matcher,
  featureSets,
  deserializeFeatureSet,
  serializeFeatureSet
)

inline fun <reified T : Any?> primitiveSerializerTest(
  serializer: PrimitiveSerializer<T>,
  value: T,
  encoded: ByteBuffer? = null,
  noinline matcher: ((T) -> Matcher<T>)? = null,
  featureSets: List<FeatureSet> = listOf(FeatureSet.none(), FeatureSet.all()),
  deserializeFeatureSet: FeatureSet? = FeatureSet.all(),
  serializeFeatureSet: FeatureSet? = FeatureSet.all(),
) {
  if (encoded != null) {
    if (deserializeFeatureSet != null) {
      val after = serializer.deserialize(encoded.rewind(), deserializeFeatureSet)
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
      assertThat(after, ByteBufferMatcher(encoded.rewind()))
    }
  }
  for (featureSet in featureSets) {
    testPrimitiveSerializerDirect(serializer, value, featureSet, matcher?.invoke(value))
  }
}
