/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections

fun <T> List<T>.move(value: T, pos: Int = size): List<T> {
  val newPos = pos.coerceIn(0, size)
  val oldPos = indexOf(value)

  return when {
    newPos > oldPos -> remove(value).insert(value, newPos - 1)
    newPos < oldPos -> remove(value).insert(value, newPos)
    else -> this
  }
}
