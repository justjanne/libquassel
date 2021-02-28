/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections

/**
 * Returns a partitioned list of pairs
 */
fun <T> Iterable<T>.pairs(): List<Pair<T, T>> {
  return pairs { a, b -> Pair(a, b) }
}

/**
 * Returns a partitioned list of pairs transformed with the given transformer
 */
inline fun <T, R> Iterable<T>.pairs(crossinline transform: (a: T, b: T) -> R): List<R> {
  val iterator = iterator()
  val result = mutableListOf<R>()
  while (true) {
    if (!iterator.hasNext()) return result
    val first = iterator.next()
    if (!iterator.hasNext()) return result
    val second = iterator.next()
    result.add(transform(first, second))
  }
}
