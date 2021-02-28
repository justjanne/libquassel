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
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals

fun <T> testPrimitiveSerializerDirect(
  serializer: PrimitiveSerializer<T>,
  data: T,
  featureSet: FeatureSet = FeatureSet.all(),
  matcher: Matcher<T>? = null
) {
  val buffer = ChainedByteBuffer(limit = 16384)
  serializer.serialize(buffer, data, featureSet)
  val result = buffer.toBuffer()
  val after = serializer.deserialize(result, featureSet)
  assertEquals(0, result.remaining())
  if (matcher != null) {
    assertThat(after, matcher)
  } else {
    assertEquals(data, after)
  }
}
