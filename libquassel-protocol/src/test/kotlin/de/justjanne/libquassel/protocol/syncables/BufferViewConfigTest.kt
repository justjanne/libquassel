/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.syncables.state.BufferViewConfigState
import de.justjanne.libquassel.protocol.testutil.nextBufferViewConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class BufferViewConfigTest {
  @Test
  fun testEmpty() {
    val state = BufferViewConfigState(bufferViewId = 1)
    val actual = BufferViewConfig(state = state).apply {
      update(emptyMap())
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextBufferViewConfig(bufferViewId = 1)

    val actual = BufferViewConfig(
      state = BufferViewConfigState(
        bufferViewId = expected.bufferViewId,
      )
    ).apply {
      update(BufferViewConfig(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
