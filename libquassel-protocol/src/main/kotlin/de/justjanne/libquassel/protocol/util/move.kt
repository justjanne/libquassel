/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util

@Suppress("NOTHING_TO_INLINE")
inline fun <T> List<T>.move(value: T, pos: Int = size): List<T> {
  val newPos = pos.coerceIn(0, size)
  val oldPos = indexOf(value)

  return if (oldPos > newPos) {
    remove(value).insert(value, newPos)
  } else if (newPos > oldPos) {
    remove(value).insert(value, newPos - 1)
  } else {
    this
  }
}
