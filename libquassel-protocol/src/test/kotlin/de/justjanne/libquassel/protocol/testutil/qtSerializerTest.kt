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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import org.hamcrest.Matcher
import java.nio.ByteBuffer

inline fun <reified T : Any?> qtSerializerTest(
  type: QtType,
  value: T,
  encoded: ByteBuffer? = null,
  noinline matcher: ((T) -> Matcher<T>)? = null,
  featureSets: List<FeatureSet> = listOf(FeatureSet.none(), FeatureSet.all()),
  deserializeFeatureSet: FeatureSet? = FeatureSet.all(),
  serializeFeatureSet: FeatureSet? = FeatureSet.all(),
) {
  if (encoded != null) {
    if (deserializeFeatureSet != null) {
      if (matcher != null) {
        testDeserialize(type.serializer<T>(), matcher(value), encoded.rewind(), deserializeFeatureSet)
      } else {
        testDeserialize(type.serializer(), value, encoded.rewind(), deserializeFeatureSet)
      }
    }
    if (serializeFeatureSet != null) {
      testSerialize(type.serializer(), value, encoded.rewind(), serializeFeatureSet)
    }
  }
  for (featureSet in featureSets) {
    testPrimitiveSerializerDirect(type.serializer(), value, featureSet, matcher?.invoke(value))
    testQtSerializerVariant(type, value, featureSet, matcher?.invoke(value))
  }
}

inline fun <reified T : Any?> qtSerializerTest(
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
      if (matcher != null) {
        testDeserialize(serializer, matcher(value), encoded.rewind(), deserializeFeatureSet)
      } else {
        testDeserialize(serializer, value, encoded.rewind(), deserializeFeatureSet)
      }
    }
    if (serializeFeatureSet != null) {
      testSerialize(serializer, value, encoded.rewind(), serializeFeatureSet)
    }
  }
  for (featureSet in featureSets) {
    testPrimitiveSerializerDirect(serializer, value, featureSet, matcher?.invoke(value))
  }
}
