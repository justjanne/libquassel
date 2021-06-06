/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections

fun <T> List<T>.insert(value: T, pos: Int = size): List<T> {
  return if (pos <= 0) {
    listOf(value) + this
  } else if (pos >= size) {
    this + value
  } else {
    val before = subList(0, pos)
    val after = subList(pos, size)

    before + value + after
  }
}
