/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.syncables.common.BufferViewManager
import de.justjanne.libquassel.protocol.syncables.state.BufferViewManagerState
import de.justjanne.libquassel.protocol.testutil.nextBufferViewManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class BufferViewManagerTest {
  @Test
  fun testEmpty() {
    val actual = BufferViewManager().apply {
      update(emptyMap())
    }.state()

    assertEquals(BufferViewManagerState(), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextBufferViewManager()

    val actual = BufferViewManager().apply {
      update(BufferViewManager(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
