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
import de.justjanne.libquassel.protocol.models.network.ChannelModeType
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.testutil.nextNetwork
import de.justjanne.libquassel.protocol.testutil.nextString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertNotEquals

class NetworkTest {
  @Test
  fun testEmpty() {
    val state = NetworkState(networkId = NetworkId(1))
    val actual = Network(state = state).apply {
      fromVariantMap(emptyMap())
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val networkId = NetworkId(random.nextInt())
    val expected = random.nextNetwork(networkId)

    val actual = Network(state = NetworkState(networkId = networkId)).apply {
      fromVariantMap(Network(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class User {
    @Test
    fun addNew() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircUserCount()
      assertNotEquals(0, sizeBefore)
      network.addIrcUser(random.nextString())
      assertEquals(sizeBefore + 1, network.ircUserCount())
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      assertNotEquals(0, network.ircUserCount())
      val existing = network.ircUsers().first()
      assertEquals(existing, network.newIrcUser(existing.hostMask()))
    }

    @Test
    fun removeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircUserCount()
      assertNotEquals(0, sizeBefore)
      network.removeIrcUser(network.ircUsers().first())
      assertEquals(sizeBefore - 1, network.ircUserCount())
    }
  }

  @Nested
  inner class Channel {
    @Test
    fun addNew() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircChannelCount()
      assertNotEquals(0, sizeBefore)
      network.addIrcChannel(random.nextString())
      assertEquals(sizeBefore + 1, network.ircChannelCount())
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      assertNotEquals(0, network.ircUserCount())
      val existing = network.ircChannels().first()
      assertEquals(existing, network.newIrcChannel(existing.name()))
    }

    @Test
    fun removeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircChannelCount()
      assertNotEquals(0, sizeBefore)
      network.removeIrcChannel(network.ircChannels().first())
      assertEquals(sizeBefore - 1, network.ircChannelCount())
    }
  }

  @Nested
  inner class Support {
    @Test
    fun addNew() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.supports().size
      assertNotEquals(0, sizeBefore)
      network.addSupport(random.nextString(), random.nextString())
      assertEquals(sizeBefore + 1, network.supports().size)
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.supports().size
      assertNotEquals(0, sizeBefore)
      network.addSupport(network.supports().keys.first(), random.nextString())
      assertEquals(sizeBefore, network.supports().size)
    }

    @Test
    fun removeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.supports().size
      assertNotEquals(0, sizeBefore)
      network.removeSupport(network.supports().keys.first())
      assertEquals(sizeBefore - 1, network.supports().size)
    }
  }

  @Nested
  inner class Capability {
    @Test
    fun addNew() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.caps().size
      assertNotEquals(0, sizeBefore)
      network.addCap(random.nextString(), random.nextString())
      assertEquals(sizeBefore + 1, network.caps().size)
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.caps().size
      assertNotEquals(0, sizeBefore)
      network.addCap(network.caps().keys.first(), random.nextString())
      assertEquals(sizeBefore, network.caps().size)
    }

    @Test
    fun acknowledgeNew() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.capsEnabled().size
      assertNotEquals(0, sizeBefore)
      network.acknowledgeCap(random.nextString())
      assertEquals(sizeBefore + 1, network.capsEnabled().size)
    }

    @Test
    fun acknowledgeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.capsEnabled().size
      assertNotEquals(0, sizeBefore)
      network.acknowledgeCap(network.capsEnabled().first())
      assertEquals(sizeBefore, network.capsEnabled().size)
    }

    @Test
    fun removeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.caps().size
      assertNotEquals(0, sizeBefore)
      network.removeCap(network.caps().keys.first())
      assertEquals(sizeBefore - 1, network.caps().size)
    }

    @Test
    fun clear() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.caps().size
      assertNotEquals(0, sizeBefore)
      network.clearCaps()
      assertEquals(0, network.caps().size)
      assertEquals(0, network.capsEnabled().size)
    }
  }

  @Nested
  inner class ChannelModes {
    @Test
    fun usual() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "CHANMODES" to "eIbq,k,flj,CFLMPQScgimnprstuz"
          )
        )
      )

      assertEquals(
        mapOf(
          ChannelModeType.A_CHANMODE to setOf('e', 'I', 'b', 'q'),
          ChannelModeType.B_CHANMODE to setOf('k'),
          ChannelModeType.C_CHANMODE to setOf('f', 'l', 'j'),
          ChannelModeType.D_CHANMODE to setOf(
            'C', 'F', 'L', 'M', 'P', 'Q', 'S', 'c', 'g', 'i', 'm', 'n', 'p', 'r', 's', 't', 'u', 'z'
          ),
        ),
        network.channelModes()
      )
    }

    @Test
    fun blank() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "CHANMODES" to ""
          )
        )
      )

      assertEquals(
        mapOf<ChannelModeType, Set<Char>>(
          ChannelModeType.A_CHANMODE to emptySet(),
          ChannelModeType.B_CHANMODE to emptySet(),
          ChannelModeType.C_CHANMODE to emptySet(),
          ChannelModeType.D_CHANMODE to emptySet(),
        ),
        network.channelModes()
      )
    }

    @Test
    fun empty() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = emptyMap()
        )
      )

      assertEquals(
        mapOf<ChannelModeType, Set<Char>>(
          ChannelModeType.A_CHANMODE to emptySet(),
          ChannelModeType.B_CHANMODE to emptySet(),
          ChannelModeType.C_CHANMODE to emptySet(),
          ChannelModeType.D_CHANMODE to emptySet(),
        ),
        network.channelModes()
      )
    }

    @Test
    fun wrongData() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "CHANMODES" to "a,b,c,d,e"
          )
        )
      )

      assertEquals(
        mapOf(
          ChannelModeType.A_CHANMODE to setOf('a'),
          ChannelModeType.B_CHANMODE to setOf('b'),
          ChannelModeType.C_CHANMODE to setOf('c'),
          ChannelModeType.D_CHANMODE to setOf('d'),
        ),
        network.channelModes()
      )
    }
  }

  @Nested
  inner class Prefixes {
    @Test
    fun usual() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "PREFIX" to "(@+)ov"
          )
        )
      )

      assertEquals(
        listOf('@', '+'),
        network.prefixes()
      )

      assertEquals(
        listOf('o', 'v'),
        network.prefixModes()
      )
    }

    @Test
    fun wrongFormatting() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "PREFIX" to "(@+]ov"
          )
        )
      )

      assertEquals(
        listOf('@', '+'),
        network.prefixes()
      )

      assertEquals(
        listOf('o', 'v'),
        network.prefixModes()
      )
    }

    @Test
    fun onlyPrefixes() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "PREFIX" to "@+"
          )
        )
      )

      assertEquals(
        listOf('@', '+'),
        network.prefixes()
      )

      assertEquals(
        listOf('o', 'v'),
        network.prefixModes()
      )
    }

    @Test
    fun onlyModes() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "PREFIX" to "ov"
          )
        )
      )

      assertEquals(
        listOf('@', '+'),
        network.prefixes()
      )

      assertEquals(
        listOf('o', 'v'),
        network.prefixModes()
      )
    }

    @Test
    fun blank() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "PREFIX" to ""
          )
        )
      )

      assertEquals(
        listOf('~', '&', '@', '%', '+'),
        network.prefixes()
      )

      assertEquals(
        listOf('q', 'a', 'o', 'h', 'v'),
        network.prefixModes()
      )
    }

    @Test
    fun wrongContent() {
      val network = Network(
        state = NetworkState(
          networkId = NetworkId(1),
          supports = mapOf(
            "PREFIX" to "12345"
          )
        )
      )

      assertEquals(
        listOf('~', '&', '@', '%', '+'),
        network.prefixes()
      )

      assertEquals(
        listOf('q', 'a', 'o', 'h', 'v'),
        network.prefixModes()
      )
    }
  }
}
