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

class InsertTest {
  @Test
  fun whenListIsEmptyReturnsSingletonList() {
    for (i in -1024..1024) {
      assertEquals(
        listOf(7),
        emptyList<Int>().insert(7, i)
      )
    }
  }

  @Test
  fun prependsForNegativeOrNull() {
    for (i in -1024..0) {
      assertEquals(
        listOf(7, 1, 2, 3),
        listOf(1, 2, 3).insert(7, i)
      )
    }
  }

  @Test
  fun appendsForOutOfBounds() {
    for (i in 3..1024) {
      assertEquals(
        listOf(1, 2, 3, 7),
        listOf(1, 2, 3).insert(7, i)
      )
    }
  }

  @Test
  fun appendsForNoParameter() {
    assertEquals(
      listOf(1, 2, 3, 7),
      listOf(1, 2, 3).insert(7)
    )
  }

  @Test
  fun isEquivalentToMutableAdd() {
    for (i in -1024..1024) {
      val before = listOf(1, 2, 3, 4, 5).shuffled()
      val afterMutable = before.toMutableList().apply {
        add(i.coerceIn(0..before.size), 7)
      }
      val afterImmutable = before.insert(7, i)
      assertEquals(afterMutable, afterImmutable)
    }
  }
}
