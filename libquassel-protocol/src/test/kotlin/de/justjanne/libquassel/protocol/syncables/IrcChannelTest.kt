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
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
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
      update(emptyMap())
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
      update(IrcChannel(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class Setters {
    @Test
    fun testTopic() {
      val random = Random(1337)
      val channel = IrcChannel(state = random.nextIrcChannel(NetworkId(random.nextInt())))

      assertNotEquals("IMPLEMENTATION DEFINED CONTROVERSY", channel.topic())
      channel.setTopic("IMPLEMENTATION DEFINED CONTROVERSY")
      assertEquals("IMPLEMENTATION DEFINED CONTROVERSY", channel.topic())
    }

    @Test
    fun testPassword() {
      val random = Random(1337)
      val channel = IrcChannel(state = random.nextIrcChannel(NetworkId(random.nextInt())))

      assertNotEquals("hunter2", channel.password())
      channel.setPassword("hunter2")
      assertEquals("hunter2", channel.password())
    }

    @Test
    fun testEncrypted() {
      val random = Random(1337)
      val channel = IrcChannel(state = random.nextIrcChannel(NetworkId(random.nextInt())))

      channel.setEncrypted(false)
      assertEquals(false, channel.isEncrypted())
      channel.setEncrypted(true)
      assertEquals(true, channel.isEncrypted())
    }
  }
}
