/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.syncables.common.BufferSyncer
import de.justjanne.libquassel.protocol.syncables.state.BufferSyncerState
import de.justjanne.libquassel.protocol.testutil.nextBufferSyncer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class BufferSyncerTest {
  @Test
  fun testEmpty() {
    val actual = BufferSyncer().apply {
      update(emptyMap())
    }.state()

    assertEquals(BufferSyncerState(), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextBufferSyncer()
      // bufferInfos are not intended to be serialized
      .copy(bufferInfos = emptyMap())

    val actual = BufferSyncer().apply {
      update(BufferSyncer(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
