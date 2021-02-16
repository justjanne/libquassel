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

fun <T> Iterable<T>.triples(): List<Triple<T, T, T>> {
  return triples { a, b, c -> Triple(a, b, c) }
}

inline fun <T, R> Iterable<T>.triples(transform: (a: T, b: T, c: T) -> R): List<R> {
  val iterator = iterator()
  val result = mutableListOf<R>()
  while (true) {
    if (!iterator.hasNext()) return result
    val first = iterator.next()
    if (!iterator.hasNext()) return result
    val second = iterator.next()
    if (!iterator.hasNext()) return result
    val third = iterator.next()
    result.add(transform(first, second, third))
  }
}
