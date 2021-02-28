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

class BomMatcherString(private val expected: String?) : BaseMatcher<String?>() {
  private val malformed = charArrayOf(
    '￾', '﻿'
  )

  override fun describeTo(description: Description?) {
    description?.appendText(expected)
  }

  override fun matches(item: Any?) =
    (item as? String)?.endsWith(expected?.trimStart(*malformed) ?: "") == true
}
