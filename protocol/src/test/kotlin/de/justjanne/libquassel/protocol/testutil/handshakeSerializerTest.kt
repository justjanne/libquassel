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
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.serializers.HandshakeMessageSerializer
import de.justjanne.libquassel.protocol.testutil.matchers.ByteBufferMatcher
import de.justjanne.libquassel.protocol.util.withRewind
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import java.nio.ByteBuffer

inline fun <reified T : HandshakeMessage> handshakeSerializerTest(
  value: T,
  encoded: ByteBuffer? = null,
  noinline matcher: ((T?) -> Matcher<T?>)? = null,
  featureSets: List<FeatureSet> = listOf(FeatureSet.none(), FeatureSet.all()),
  deserializeFeatureSet: FeatureSet? = FeatureSet.all(),
  serializeFeatureSet: FeatureSet? = FeatureSet.all(),
) {
  if (encoded != null) {
    if (deserializeFeatureSet != null) {
      val after = HandshakeMessageSerializer.deserialize(encoded, deserializeFeatureSet) as? T
      if (matcher != null) {
        assertThat(after, matcher(value))
      } else {
        assertEquals(value, after)
      }
    }
    if (serializeFeatureSet != null) {
      assertThat(
        useChainedByteBuffer {
          HandshakeMessageSerializer.serialize(it, value, serializeFeatureSet)
        },
        ByteBufferMatcher(encoded.withRewind())
      )
    }
  }
  for (featureSet in featureSets) {
    val after = HandshakeMessageSerializer.deserialize(
      useChainedByteBuffer {
        HandshakeMessageSerializer.serialize(it, value, featureSet)
      },
      featureSet
    )
    assertEquals(T::class.java, after::class.java)
    if (matcher != null) {
      assertThat(after as? T, matcher(value))
    } else {
      assertEquals(value, after)
    }
  }
}
