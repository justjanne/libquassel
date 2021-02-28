/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
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
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

@Tag("QtSerializerTest")
class QDateSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QDateSerializer,
      QtType.QDate.serializer<LocalDate>(),
    )
  }

  @Test
  fun testEpoch() = primitiveSerializerTest(
    QDateSerializer,
    LocalDate
      .of(1970, 1, 1),
    byteBufferOf(0, 37, 61, -116),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testNormalCase() = primitiveSerializerTest(
    QDateSerializer,
    LocalDate
      .of(2019, Month.JANUARY, 15),
    byteBufferOf(0, 37, -125, -125),
    matcher = ::TemporalMatcher
  )
}
