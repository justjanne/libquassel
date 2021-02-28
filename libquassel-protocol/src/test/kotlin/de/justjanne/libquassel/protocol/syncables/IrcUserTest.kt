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
import de.justjanne.libquassel.protocol.syncables.state.IrcUserState
import de.justjanne.libquassel.protocol.testutil.nextIrcUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class IrcUserTest {
  @Test
  fun testEmpty() {
    val state = IrcUserState(
      network = NetworkId(1),
      nick = "nick",
      user = "user",
      host = "host"
    )
    val actual = IrcUser(state = state).apply {
      fromVariantMap(emptyMap())
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextIrcUser(NetworkId(random.nextInt()))

    val actual = IrcUser(
      state = IrcUserState(
        network = expected.network,
        nick = expected.nick,
        user = expected.user,
        host = expected.host
      )
    ).apply {
      fromVariantMap(IrcUser(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
