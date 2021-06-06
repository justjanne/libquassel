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
import de.justjanne.libquassel.protocol.models.network.ChannelModes
import de.justjanne.libquassel.protocol.syncables.common.IrcChannel
import de.justjanne.libquassel.protocol.syncables.common.IrcUser
import de.justjanne.libquassel.protocol.syncables.common.Network
import de.justjanne.libquassel.protocol.syncables.state.IrcChannelState
import de.justjanne.libquassel.protocol.testutil.mocks.EmptySession
import de.justjanne.libquassel.protocol.testutil.nextIrcChannel
import de.justjanne.libquassel.protocol.testutil.nextNetwork
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
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

  @Nested
  inner class AddChannelMode {
    @Test
    fun noSession() {
      val random = Random(1337)
      val channel = IrcChannel(state = random.nextIrcChannel(NetworkId(random.nextInt())))

      val channelModes = channel.state().channelModes
      channel.addChannelMode('a', value = "*!*@*")
      assertEquals(channelModes, channel.state().channelModes)
    }

    @Test
    fun chanmodeUnknown() {
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "a,b,c,d"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(session, it.value.state())
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
      channel.addChannelMode('e', value = "*!*@*")
      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
    }

    @Test
    fun chanmodeA() {
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "a,b,c,d"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(session, it.value.state())
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
      channel.addChannelMode('a', value = "*!*@*")
      assertEquals(
        mapOf(
          'a' to setOf("*!*@*")
        ),
        channel.state().channelModes.a
      )
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
      channel.addChannelMode('a', value = "user!ident@host")
      assertEquals(
        mapOf(
          'a' to setOf("*!*@*", "user!ident@host")
        ),
        channel.state().channelModes.a
      )
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)

      assertThrows(IllegalArgumentException::class.java) {
        channel.addChannelMode('a', value = null)
      }
    }

    @Test
    fun chanmodeB() {
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "a,b,c,d"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(session, it.value.state())
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
      channel.addChannelMode('b', value = "*!*@*")
      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(
        mapOf(
          'b' to "*!*@*"
        ),
        channel.state().channelModes.b
      )
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)

      assertThrows(IllegalArgumentException::class.java) {
        channel.addChannelMode('b', value = null)
      }
    }

    @Test
    fun chanmodeC() {
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "a,b,c,d"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(session, it.value.state())
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
      channel.addChannelMode('c', value = "*!*@*")
      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(
        mapOf(
          'c' to "*!*@*"
        ),
        channel.state().channelModes.c
      )
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)

      assertThrows(IllegalArgumentException::class.java) {
        channel.addChannelMode('c', value = null)
      }
    }

    @Test
    fun chanmodeD() {
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "a,b,c,d"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(session, it.value.state())
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
      channel.addChannelMode('d', value = "*!*@*")
      assertEquals(emptyMap<Char, Set<String>>(), channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(setOf('d'), channel.state().channelModes.d)
    }
  }

  @Nested
  inner class RemoveChannelMode {
    @Test
    fun noSession() {

      val expected = ChannelModes(
        a = mapOf(
          'a' to setOf("a1", "a2"),
          'A' to setOf("A1", "A2")
        ),
        b = mapOf(
          'b' to "b1",
          'B' to "B1"
        ),
        c = mapOf(
          'c' to "c1",
          'C' to "C1"
        ),
        d = setOf('d', 'D')
      )

      val random = Random(1337)
      val channel = IrcChannel(
        state = random.nextIrcChannel(NetworkId(random.nextInt()))
          .copy(channelModes = expected)
      )

      channel.removeChannelMode('a', value = "a1")
      assertEquals(expected, channel.state().channelModes)
    }

    @Test
    fun chanmodeUnknown() {
      val expected = ChannelModes(
        a = mapOf(
          'a' to setOf("a1", "a2"),
          'A' to setOf("A1", "A2")
        ),
        b = mapOf(
          'b' to "b1",
          'B' to "B1"
        ),
        c = mapOf(
          'c' to "c1",
          'C' to "C1"
        ),
        d = setOf('d', 'D')
      )

      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "a,b,c,d"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(
                session,
                it.value.state()
                  .copy(channelModes = expected)
              )
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      channel.removeChannelMode('e', value = "*!*@*")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
    }

    @Test
    fun chanmodeA() {
      val expected = ChannelModes(
        a = mapOf(
          'a' to setOf("a1", "a2")
        ),
        b = mapOf(
          'b' to "b1"
        ),
        c = mapOf(
          'c' to "c1"
        ),
        d = setOf('d')
      )
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "aA,bB,cC,dD"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(
                session,
                it.value.state()
                  .copy(channelModes = expected)
              )
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('A'))
      assertTrue(channel.hasMode('a'))
      assertEquals(emptySet<String>(), channel.modeValues('A'))
      assertEquals(setOf("a1", "a2"), channel.modeValues('a'))
      channel.removeChannelMode('A', value = "a1")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('A'))
      assertTrue(channel.hasMode('a'))
      assertEquals(emptySet<String>(), channel.modeValues('A'))
      assertEquals(setOf("a1", "a2"), channel.modeValues('a'))
      channel.removeChannelMode('a', value = "a1")
      assertEquals(
        mapOf(
          'a' to setOf("a2"),
        ),
        channel.state().channelModes.a
      )
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('A'))
      assertTrue(channel.hasMode('a'))
      assertEquals(emptySet<String>(), channel.modeValues('A'))
      assertEquals(setOf("a2"), channel.modeValues('a'))
      channel.removeChannelMode('a', value = "a1")
      assertEquals(
        mapOf(
          'a' to setOf("a2"),
        ),
        channel.state().channelModes.a
      )
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('A'))
      assertTrue(channel.hasMode('a'))
      assertEquals(emptySet<String>(), channel.modeValues('A'))
      assertEquals(setOf("a2"), channel.modeValues('a'))

      assertThrows(IllegalArgumentException::class.java) {
        channel.removeChannelMode('a', value = null)
      }
    }

    @Test
    fun chanmodeB() {
      val expected = ChannelModes(
        a = mapOf(
          'a' to setOf("a1", "a2")
        ),
        b = mapOf(
          'b' to "b1"
        ),
        c = mapOf(
          'c' to "c1"
        ),
        d = setOf('d')
      )
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "aA,bB,cC,dD"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(
                session,
                it.value.state()
                  .copy(channelModes = expected)
              )
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('B'))
      assertTrue(channel.hasMode('b'))
      channel.removeChannelMode('B', value = "b1")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('B'))
      assertTrue(channel.hasMode('b'))
      channel.removeChannelMode('b', value = "b1")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('B'))
      assertFalse(channel.hasMode('b'))
      channel.removeChannelMode('b', value = "b2")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('B'))
      assertFalse(channel.hasMode('b'))
    }

    @Test
    fun chanmodeC() {
      val expected = ChannelModes(
        a = mapOf(
          'a' to setOf("a1", "a2")
        ),
        b = mapOf(
          'b' to "b1"
        ),
        c = mapOf(
          'c' to "c1"
        ),
        d = setOf('d')
      )
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "aA,bB,cC,dD"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(
                session,
                it.value.state()
                  .copy(channelModes = expected)
              )
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('C'))
      assertTrue(channel.hasMode('c'))
      assertEquals("", channel.modeValue('C'))
      assertEquals("c1", channel.modeValue('c'))
      channel.removeChannelMode('C', value = "c1")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('C'))
      assertTrue(channel.hasMode('c'))
      assertEquals("", channel.modeValue('C'))
      assertEquals("c1", channel.modeValue('c'))
      channel.removeChannelMode('c', value = "c1")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('C'))
      assertFalse(channel.hasMode('c'))
      assertEquals("", channel.modeValue('C'))
      assertEquals("", channel.modeValue('c'))
      channel.removeChannelMode('c', value = "c2")
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(emptyMap<Char, String>(), channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('C'))
      assertFalse(channel.hasMode('c'))
      assertEquals("", channel.modeValue('C'))
      assertEquals("", channel.modeValue('c'))
    }

    @Test
    fun chanmodeD() {
      val expected = ChannelModes(
        a = mapOf(
          'a' to setOf("a1", "a2")
        ),
        b = mapOf(
          'b' to "b1"
        ),
        c = mapOf(
          'c' to "c1"
        ),
        d = setOf('d')
      )
      val random = Random(1337)
      val session = ChannelMockSession()
      val network = Network(
        session,
        state = random.nextNetwork(NetworkId(random.nextInt())).run {
          copy(
            supports = mapOf(
              "CHANMODES" to "aA,bB,cC,dD"
            ),
            ircChannels = ircChannels.mapValues {
              IrcChannel(
                session,
                it.value.state()
                  .copy(channelModes = expected)
              )
            },
            ircUsers = ircUsers.mapValues {
              IrcUser(session, it.value.state())
            }
          )
        }
      )
      session.networks.add(network)
      val channel = network.state().ircChannels.values.first()

      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('D'))
      assertTrue(channel.hasMode('d'))
      channel.removeChannelMode('D')
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertEquals(expected.d, channel.state().channelModes.d)
      assertFalse(channel.hasMode('D'))
      assertTrue(channel.hasMode('d'))
      channel.removeChannelMode('d')
      assertEquals(expected.a, channel.state().channelModes.a)
      assertEquals(expected.b, channel.state().channelModes.b)
      assertEquals(expected.c, channel.state().channelModes.c)
      assertFalse(channel.hasMode('D'))
      assertFalse(channel.hasMode('d'))
      assertEquals(emptySet<Char>(), channel.state().channelModes.d)
    }
  }

  class ChannelMockSession : EmptySession() {
    val networks = mutableListOf<Network>()
    override fun network(id: NetworkId) = networks.find { it.networkId() == id }
  }
}
