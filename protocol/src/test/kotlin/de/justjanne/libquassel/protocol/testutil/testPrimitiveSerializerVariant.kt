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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.qt.QVariantSerializer
import de.justjanne.libquassel.protocol.variant.qVariant
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals

inline fun <reified T> testPrimitiveSerializerVariant(
  type: QuasselType,
  data: T,
  featureSet: FeatureSet = FeatureSet.all(),
  matcher: Matcher<in T>? = null
) {
  val buffer = ChainedByteBuffer(limit = 16384)
  QVariantSerializer.serialize(buffer, qVariant(data, type), featureSet)
  val result = buffer.toBuffer()
  val after = QVariantSerializer.deserialize(result, featureSet)
  assertEquals(0, result.remaining())
  if (matcher != null) {
    @Suppress("UNCHECKED_CAST")
    assertThat(after.data as T, matcher)
  } else {
    assertEquals(data, after.data)
  }
}

inline fun <reified T> testPrimitiveSerializerVariant(
  type: QtType,
  data: T,
  featureSet: FeatureSet = FeatureSet.all(),
  matcher: Matcher<in T>? = null
) {
  val buffer = ChainedByteBuffer(limit = 16384)
  QVariantSerializer.serialize(buffer, qVariant(data, type), featureSet)
  val result = buffer.toBuffer()
  val after = QVariantSerializer.deserialize(result, featureSet)
  assertEquals(0, result.remaining())
  if (matcher != null) {
    @Suppress("UNCHECKED_CAST")
    assertThat(after.data as T, matcher)
  } else {
    assertEquals(data, after.data)
  }
}
