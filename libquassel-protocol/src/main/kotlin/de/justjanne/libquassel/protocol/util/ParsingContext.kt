/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util

import de.justjanne.libquassel.annotations.Generated
import de.justjanne.libquassel.protocol.util.expansion.Expansion
import java.util.function.Supplier

internal abstract class ParsingContext<T>(
  internal val text: String
) {
  protected abstract val matchers: List<Supplier<T?>>

  internal var position = 0

  fun parse(): List<T> {
    val result = mutableListOf<T>()
    while (position < text.length) {
      for (matcher in matchers) {
        val match = matcher.get()
        if (match != null) {
          result.add(match)
          continue
        }
      }
    }
    return result
  }

  @Generated
  protected inline fun match(
    vararg patterns: String,
    crossinline function: (String) -> Expansion
  ) = Supplier {
    for (pattern in patterns) {
      if (text.startsWith(pattern, startIndex = position)) {
        position += pattern.length
        return@Supplier function(pattern)
      }
    }
    return@Supplier null
  }

  @Generated
  protected inline fun match(
    vararg patterns: Regex,
    crossinline function: (String, List<String>) -> Expansion
  ) = Supplier {
    for (pattern in patterns) {
      val match = pattern.find(text, startIndex = position)
      if (match != null && match.range.first == position) {
        position = match.range.last + 1
        return@Supplier function(match.value, match.groupValues)
      }
    }
    return@Supplier null
  }
}
