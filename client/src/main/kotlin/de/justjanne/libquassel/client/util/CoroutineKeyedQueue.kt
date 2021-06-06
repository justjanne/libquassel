/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.util

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CoroutineKeyedQueue<Key, Value> {
  private val waiting = mutableMapOf<Key, MutableList<Continuation<Value>>>()
  suspend fun wait(vararg keys: Key): Value = suspendCoroutine {
    for (key in keys) {
      waiting.getOrPut(key, ::mutableListOf).add(it)
    }
  }

  fun resume(key: Key, value: Value) {
    val continuations = waiting[key].orEmpty().distinct()
    for (continuation in continuations) {
      continuation.resume(value)
    }
    for (it in waiting.keys) {
      waiting[it]?.removeAll(continuations)
    }
  }
}
