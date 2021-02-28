/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil.matchers

import org.hamcrest.BaseMatcher
import org.hamcrest.Description

class MapMatcher<K, V>(
  private val expected: Map<K, V>
) : BaseMatcher<Map<K, V>>() {
  override fun describeTo(description: Description?) {
    description?.appendText(expected.toString())
  }

  override fun describeMismatch(item: Any?, description: Description?) {
    if (item is Map<*, *>) {
      for (key in expected.keys) {
        if (!item.containsKey(key)) {
          description?.appendText(" did not have key $key")
        }
        if (expected[key] != item[key]) {
          description?.appendText(" key $key was: ${item[key]} instead of ${expected[key]}")
        }
      }
    } else {
      description?.appendText("was: $item")
    }
  }

  override fun matches(item: Any?): Boolean {
    if (item is Map<*, *>) {
      for (key in expected.keys) {
        if (!item.containsKey(key)) {
          return false
        }
        if (expected[key] != item[key]) {
          return false
        }
      }
      return true
    } else {
      return false
    }
  }
}
