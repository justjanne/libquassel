/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.matchers.TemporalMatcher
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalTime

@Tag("QtSerializerTest")
class QTimeSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QTimeSerializer,
      QtType.QTime.serializer<LocalTime>(),
    )
  }

  @Test
  fun testEpoch() = primitiveSerializerTest(
    QTimeSerializer,
    LocalTime
      .of(0, 0),
    byteBufferOf(0, 0, 0, 0),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testNormalCase() = primitiveSerializerTest(
    QTimeSerializer,
    LocalTime
      .of(20, 25),
    byteBufferOf(0x04u, 0x61u, 0x85u, 0x60u),
    matcher = ::TemporalMatcher
  )
}
