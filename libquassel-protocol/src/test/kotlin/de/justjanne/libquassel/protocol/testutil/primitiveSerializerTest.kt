/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.testutil

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.useChainedByteBuffer
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.testutil.matchers.ByteBufferMatcher
import de.justjanne.libquassel.protocol.util.withRewind
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
  for (featureSet in featureSets) {
    testPrimitiveSerializerDirect(serializer, value, featureSet, matcher?.invoke(value))
  }
}
