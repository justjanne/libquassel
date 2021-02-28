/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PairsTest {
  @Test
  fun testFunctionality() {
    val list = (0 until 1024).map {
      Pair(Math.random(), Math.random())
    }
    assertEquals(
      list,
      PairsProxy.call(
        list.flatMap { listOf(it.first, it.second) },
        ::Pair
      )
    )
    assertEquals(
      list,
      list.flatMap { listOf(it.first, it.second) }.pairs()
    )
  }

  @Test
  fun testMalformedPairs() {
    val list = (0 until 1024).map {
      Pair(Math.random(), Math.random())
    }
    assertEquals(
      list.subList(0, 256),
      PairsProxy.call(
        list.flatMap { listOf(it.first, it.second) }
          .subList(0, 513),
        ::Pair
      )
    )
    assertEquals(
      list.subList(0, 256),
      list.flatMap { listOf(it.first, it.second) }
        .subList(0, 513).pairs()
    )
  }
}
