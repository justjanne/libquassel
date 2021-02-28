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
import org.junit.jupiter.api.assertThrows
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.chrono.JapaneseDate
import org.threeten.bp.temporal.Temporal

@Tag("QtSerializerTest")
class QDateTimeSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QDateTimeSerializer,
      QtType.QDateTime.serializer<Temporal>(),
    )
  }

  @Test
  fun testEpoch() = primitiveSerializerTest(
    QDateTimeSerializer,
    Instant.EPOCH,
    byteBufferOf(0, 37, 61, -116, 0, 0, 0, 0, 2),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testEpochAtTimezone() = primitiveSerializerTest(
    QDateTimeSerializer,
    Instant.EPOCH.atOffset(ZoneOffset.ofTotalSeconds(1234)),
    byteBufferOf(0x00u, 0x25u, 0x3Du, 0x8Cu, 0x00u, 0x12u, 0xD4u, 0x50u, 0x03u, 0x00u, 0x00u, 0x04u, 0xD2u),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testEpochByCalendarAtTimezone() = primitiveSerializerTest(
    QDateTimeSerializer,
    LocalDateTime
      .of(1970, 1, 1, 0, 0)
      .atZone(ZoneId.of("Europe/Berlin"))
      .toInstant(),
    byteBufferOf(0, 37, 61, -117, 4, -17, 109, -128, 2),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testNormalCase() = primitiveSerializerTest(
    QDateTimeSerializer,
    LocalDateTime
      .of(2019, Month.JANUARY, 15, 20, 25)
      .atZone(ZoneId.of("Europe/Berlin"))
      .toInstant(),
    byteBufferOf(0, 37, -125, -125, 4, 42, -106, -32, 2),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testLocalDateTime() = primitiveSerializerTest(
    QDateTimeSerializer,
    LocalDateTime
      .of(2019, Month.JANUARY, 15, 20, 25),
    byteBufferOf(0x00u, 0x25u, 0x83u, 0x83u, 0x04u, 0x61u, 0x85u, 0x60u, 0xFFu),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testZonedDateTime() = primitiveSerializerTest(
    QDateTimeSerializer,
    LocalDateTime
      .of(2019, Month.JANUARY, 15, 20, 25)
      .atZone(ZoneId.systemDefault()),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testUnknownDateTime() = primitiveSerializerTest(
    QDateTimeSerializer,
    LocalDateTime
      .of(2019, Month.JANUARY, 15, 20, 25),
    byteBufferOf(0x00u, 0x25u, 0x83u, 0x83u, 0x04u, 0x61u, 0x85u, 0x60u, 0xFFu),
    matcher = ::TemporalMatcher
  )

  @Test
  fun testInvalidDateTime() = primitiveSerializerTest(
    QDateTimeSerializer,
    LocalDateTime
      .of(2019, Month.JANUARY, 15, 20, 25),
    byteBufferOf(0x00u, 0x25u, 0x83u, 0x83u, 0x04u, 0x61u, 0x85u, 0x60u, 0x09u),
    matcher = ::TemporalMatcher,
    serializeFeatureSet = null,
    featureSets = emptyList(),
  )

  @Test
  fun testOldJavaDate() {
    assertThrows<IllegalArgumentException>("Unsupported Format: org.threeten.bp.chrono.JapaneseDate") {
      primitiveSerializerTest(
        QDateTimeSerializer,
        JapaneseDate.now(),
        matcher = ::TemporalMatcher
      )
    }
  }
}
