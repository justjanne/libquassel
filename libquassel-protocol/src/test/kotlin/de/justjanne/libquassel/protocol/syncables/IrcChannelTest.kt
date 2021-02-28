/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.state.IrcChannelState
import de.justjanne.libquassel.protocol.testutil.nextIrcChannel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class IrcChannelTest {
  @Test
  fun testEmpty() {
    val state = IrcChannelState(
      network = NetworkId(1),
      name = "#name"
    )
    val actual = IrcChannel(state = state).apply {
      fromVariantMap(emptyMap())
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextIrcChannel(NetworkId(random.nextInt()))

    val actual = IrcChannel(
      state = IrcChannelState(
        network = expected.network,
        name = expected.name,
      )
    ).apply {
      fromVariantMap(IrcChannel(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
