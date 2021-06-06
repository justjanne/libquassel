/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.testutil.matchers

import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.Temporal

class TemporalMatcher<T : Temporal>(
  private val expected: T
) : BaseMatcher<T>() {
  override fun describeTo(description: Description?) {
    description?.appendText(expected.toString())
  }

  private fun localDateTime(value: Any?): LocalDateTime? = when (value) {
    is ZonedDateTime -> value.toLocalDateTime()
    is OffsetDateTime -> value.toLocalDateTime()
    is LocalDateTime -> value
    is Instant -> value.atOffset(ZoneOffset.UTC).toLocalDateTime()
    is LocalDate -> value.atTime(LocalTime.MIN)
    is LocalTime -> value.atDate(LocalDate.MIN)
    null -> null
    else -> throw Exception("Unsupported date format: ${value::class.java.simpleName}")
  }

  private fun offset(value: Any?): ZoneOffset = when (value) {
    is ZonedDateTime -> value.offset
    is OffsetDateTime -> value.offset
    is LocalDateTime -> value.atZone(ZoneId.systemDefault()).toOffsetDateTime().offset
    is LocalDate -> value.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toOffsetDateTime().offset
    is LocalTime -> value.atDate(LocalDate.MIN).atZone(ZoneId.systemDefault()).toOffsetDateTime().offset
    else -> ZoneOffset.UTC
  }

  override fun describeMismatch(item: Any?, description: Description?) {
    if (localDateTime(item) != localDateTime(expected)) {
      description?.appendText("Expected local date time of ")
      description?.appendValue(localDateTime(expected))
      description?.appendText(" but got")
      description?.appendValue(localDateTime(item))
      description?.appendText(".")
    }
    if (offset(item) != offset(expected)) {
      description?.appendText("Expected zone offset of ")
      description?.appendValue(offset(expected))
      description?.appendText(" but got ")
      description?.appendValue(offset(item))
      description?.appendText(".")
    }
    super.describeMismatch(item, description)
  }

  override fun matches(item: Any?): Boolean {
    return localDateTime(expected) == localDateTime(item) &&
      (offset(expected) == offset(item))
  }
}
