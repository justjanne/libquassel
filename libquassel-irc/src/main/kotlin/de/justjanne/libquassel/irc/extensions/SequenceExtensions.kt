/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.irc.extensions

internal fun <T> Sequence<T>.collapse(callback: (T, T) -> T?) = sequence<T> {
  var prev: T? = null
  for (item in iterator()) {
    if (prev != null) {
      val collapsed = callback(prev, item)
      if (collapsed == null) {
        yield(prev)
        prev = item
      } else {
        prev = collapsed
      }
    } else {
      prev = item
    }
  }
  if (prev != null) {
    yield(prev)
  }
}
