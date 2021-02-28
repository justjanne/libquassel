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

class RemoveTest {
  @Test
  fun isEquivalentToMutableRemove() {
    for (i in -1024..1024) {
      val before = listOf(1, 2, 3, 4, 5).shuffled()
      val afterMutable = before.toMutableList().apply {
        remove(i)
      }
      val afterImmutable = before.remove(i)
      assertEquals(afterMutable, afterImmutable)
    }
  }
}
