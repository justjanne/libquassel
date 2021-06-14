/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.syncables.common.CoreInfo
import de.justjanne.libquassel.protocol.syncables.state.CoreInfoState
import de.justjanne.libquassel.protocol.testutil.nextCoreInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class CoreInfoTest {
  @Test
  fun testEmpty() {
    val actual = CoreInfo().apply {
      update(emptyMap())
    }.state()

    assertEquals(CoreInfoState(), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextCoreInfo()

    val actual = CoreInfo().apply {
      update(CoreInfo(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
