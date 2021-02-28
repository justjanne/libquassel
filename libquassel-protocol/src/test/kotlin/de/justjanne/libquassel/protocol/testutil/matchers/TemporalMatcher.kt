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
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.Temporal

class TemporalMatcher<T : Temporal>(
  private val expected: T
) : BaseMatcher<T>() {
  override fun describeTo(description: Description?) {
    description?.appendText(expected.toString())
  }

  override fun matches(item: Any?): Boolean {
    return when {
      expected is ZonedDateTime && item is ZonedDateTime ->
        expected == item
      expected is ZonedDateTime && item is OffsetDateTime ->
        expected.toOffsetDateTime() == item
      expected is OffsetDateTime && item is OffsetDateTime ->
        expected == item
      expected is LocalDateTime && item is LocalDateTime ->
        expected == item
      expected is LocalTime && item is LocalTime ->
        expected == item
      expected is LocalDate && item is LocalDate ->
        expected == item
      expected is Instant && item is Instant ->
        expected == item
      else ->
        false
    }
  }
}
