/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MoveTest {
  @Test
  fun appendsForOutOfBounds() {
    for (i in 6..1024) {
      assertEquals(
        listOf(1, 2, 3, 4, 5, 7),
        listOf(1, 2, 7, 3, 4, 5).move(7, i)
      )
    }
  }

  @Test
  fun appendsForNoParameter() {
    assertEquals(
      listOf(1, 2, 3, 4, 5, 7),
      listOf(1, 2, 7, 3, 4, 5).move(7)
    )
  }

  @Test
  fun prependsForNegativeOrNull() {
    for (i in -1024..0) {
      assertEquals(
        listOf(7, 1, 2, 3, 4, 5),
        listOf(1, 2, 7, 3, 4, 5).move(7, i)
      )
    }
  }

  @Test
  fun noopIfUnchanged() {
    for (i in 0..1024) {
      val data = listOf(1, 2, 3, 4, 5, 7).shuffled()
      assertEquals(
        data,
        data.move(7, data.indexOf(7))
      )
    }
  }

  @Test
  fun movesCorrectly() {
    val data = listOf(1, 2, 3, 4, 5, 7).shuffled()
    assertEquals(
      listOf('a', 'c', 'd', 'e', 'b', 'f'),
      listOf('a', 'b', 'c', 'd', 'e', 'f').move('b', 4)
    )
  }
}
