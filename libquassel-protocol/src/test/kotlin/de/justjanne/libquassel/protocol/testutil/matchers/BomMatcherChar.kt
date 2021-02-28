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

class BomMatcherChar(private val expected: Char) : BaseMatcher<Char>() {
  private val malformed = charArrayOf(
    '\uFFFE', '\uFEFF', '\uFFFD', '﻿'
  )

  override fun describeTo(description: Description?) {
    description?.appendText(expected.toString())
  }

  override fun matches(item: Any?): Boolean {
    if (item is Char) {
      return (item == expected) || (item in malformed && expected in malformed)
    }
    return false
  }
}
