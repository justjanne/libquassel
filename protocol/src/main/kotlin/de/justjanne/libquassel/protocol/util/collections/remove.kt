/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections

fun <T> List<T>.remove(value: T): List<T> = this.filter { it != value }
fun <T> List<T>.removeAt(index: Int): List<T> {
  if (index < 0 || index >= size) return this

  val before = subList(0, index)
  val after = subList(index + 1, size)
  if (before.isEmpty()) return after
  if (after.isEmpty()) return before
  return before + after
}
