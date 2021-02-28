/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QuasselSerializerTest")
class MsgIdSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      MsgIdSerializer,
      QuasselType.MsgId.serializer<MsgId>(),
    )
  }

  @Test
  fun testZero() = primitiveSerializerTest(
    MsgIdSerializer,
    MsgId(0),
    byteBufferOf(0, 0, 0, 0, 0, 0, 0, 0),
    featureSets = listOf(FeatureSet.all())
  )

  @Test
  fun testMinimal() = primitiveSerializerTest(
    MsgIdSerializer,
    MsgId.MIN_VALUE,
    byteBufferOf(-128, 0, 0, 0, 0, 0, 0, 0),
    featureSets = listOf(FeatureSet.all())
  )

  @Test
  fun testMaximal() = primitiveSerializerTest(
    MsgIdSerializer,
    MsgId.MAX_VALUE,
    byteBufferOf(127, -1, -1, -1, -1, -1, -1, -1),
    featureSets = listOf(FeatureSet.all())
  )

  @Test
  fun testAllOnes() = primitiveSerializerTest(
    MsgIdSerializer,
    MsgId(0.inv()),
    byteBufferOf(-1, -1, -1, -1, -1, -1, -1, -1),
    featureSets = listOf(FeatureSet.all())
  )
}
