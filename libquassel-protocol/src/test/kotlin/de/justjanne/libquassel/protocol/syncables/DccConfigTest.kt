/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.syncables.common.DccConfig
import de.justjanne.libquassel.protocol.syncables.state.DccConfigState
import de.justjanne.libquassel.protocol.testutil.nextDccConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class DccConfigTest {
  @Test
  fun testEmpty() {
    val actual = DccConfig().apply {
      update(emptyMap())
    }.state()

    assertEquals(DccConfigState(), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextDccConfig()

    val actual = DccConfig().apply {
      update(DccConfig(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
