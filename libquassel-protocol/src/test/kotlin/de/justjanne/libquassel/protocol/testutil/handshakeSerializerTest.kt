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
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.serializers.HandshakeMessageSerializer
import de.justjanne.libquassel.protocol.testutil.matchers.ByteBufferMatcher
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import java.nio.ByteBuffer
import kotlin.test.assertEquals

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
        assertEquals(after, value)
      }
    }
    if (serializeFeatureSet != null) {
      assertThat(
        useChainedByteBuffer {
          HandshakeMessageSerializer.serialize(it, value, serializeFeatureSet)
        },
        ByteBufferMatcher(encoded.rewind())
      )
    }
  }
  for (featureSet in featureSets) {
    val after = HandshakeMessageSerializer.deserialize(
      useChainedByteBuffer {
        HandshakeMessageSerializer.serialize(it, value, featureSet)
      },
      featureSet
    ) as? T
    if (matcher != null) {
      assertThat(after, matcher(value))
    } else {
      assertEquals(value, after)
    }
  }
}
