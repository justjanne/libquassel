/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.network.ChannelModeType
import de.justjanne.libquassel.protocol.models.network.ConnectionState
import de.justjanne.libquassel.protocol.models.network.NetworkServer
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.testutil.MockSession
import de.justjanne.libquassel.protocol.testutil.nextNetwork
import de.justjanne.libquassel.protocol.testutil.nextString
import de.justjanne.libquassel.protocol.variant.qVariant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.random.Random

class NetworkTest {
  @Test
  fun testEmpty() {
    val state = NetworkState(networkId = NetworkId(1))
    val actual = Network(state = state).apply {
      update(emptyMap())
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testInvalid() {
    val state = NetworkState(networkId = NetworkId(1))
    val actual = Network(state = state).apply {
      update(
        mapOf(
          "connectionState" to qVariant(-2, QtType.Int),
        )
      )
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val networkId = NetworkId(random.nextInt())
    val expected = random.nextNetwork(networkId)

    val actual = Network(state = NetworkState(networkId = networkId)).apply {
      update(Network(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class Setters {
    @Test
    fun testIdentity() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(IdentityId(4), network.identity())
      network.setIdentity(IdentityId(4))
      assertEquals(IdentityId(4), network.identity())
    }

    @Test
    fun testMyNick() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("justJanne", network.myNick())
      network.setMyNick("justJanne")
      assertEquals("justJanne", network.myNick())
    }

    @Test
    fun testLatency() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(500, network.latency())
      network.setLatency(500)
      assertEquals(500, network.latency())
    }

    @Test
    fun testNetworkName() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("Freenode", network.networkName())
      network.setNetworkName("Freenode")
      assertEquals("Freenode", network.networkName())
    }

    @Test
    fun testCurrentServer() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("irc.freenode.org", network.currentServer())
      network.setCurrentServer("irc.freenode.org")
      assertEquals("irc.freenode.org", network.currentServer())
    }

    @Test
    fun testConnectionStateValid() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(ConnectionState.Initializing, network.connectionState())
      network.setConnectionState(ConnectionState.Initializing.value)
      assertEquals(ConnectionState.Initializing, network.connectionState())
    }

    @Test
    fun testConnectionStateInvalid() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(ConnectionState.Disconnected, network.connectionState())
      network.setConnectionState(-2)
      assertEquals(ConnectionState.Disconnected, network.connectionState())
    }

    @Test
    fun testServerList() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      val desired = listOf(
        NetworkServer(
          host = "irc.freenode.org",
          port = 6697u,
          useSsl = true,
          sslVerify = true,
        ),
        NetworkServer(
          host = "irc.freenode.org",
          port = 6667u,
          useSsl = false,
          sslVerify = false,
        )
      )
      assertNotEquals(desired, network.serverList())
      network.setServerList(
        desired.map {
          qVariant(it, QuasselType.NetworkServer)
        }
      )
      assertEquals(desired, network.serverList())
    }

    @Test
    fun testUseRandomServer() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setUseRandomServer(false)
      assertEquals(false, network.useRandomServer())
      network.setUseRandomServer(true)
      assertEquals(true, network.useRandomServer())
    }

    @Test
    fun testPerform() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      val value = listOf(
        "/wait 5; /ns ghost",
        null,
        "/mode -x"
      )

      val desired = listOf(
        "/wait 5; /ns ghost",
        "",
        "/mode -x"
      )

      assertNotEquals(desired, network.perform())
      network.setPerform(value)
      assertEquals(desired, network.perform())
    }

    @Test
    fun testUseAutoIdentify() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setUseAutoIdentify(false)
      assertEquals(false, network.useAutoIdentify())
      network.setUseAutoIdentify(true)
      assertEquals(true, network.useAutoIdentify())
    }

    @Test
    fun testAutoIdentifyPassword() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("hunter2", network.autoIdentifyPassword())
      network.setAutoIdentifyPassword("hunter2")
      assertEquals("hunter2", network.autoIdentifyPassword())
    }

    @Test
    fun testAutoIdentifyService() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("NickServ", network.autoIdentifyService())
      network.setAutoIdentifyService("NickServ")
      assertEquals("NickServ", network.autoIdentifyService())
    }

    @Test
    fun testUseSasl() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setUseSasl(false)
      assertEquals(false, network.useSasl())
      network.setUseSasl(true)
      assertEquals(true, network.useSasl())
    }

    @Test
    fun testSaslAccount() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("AzureDiamond", network.saslAccount())
      network.setSaslAccount("AzureDiamond")
      assertEquals("AzureDiamond", network.saslAccount())
    }

    @Test
    fun testSaslPassword() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("hunter2", network.saslPassword())
      network.setSaslPassword("hunter2")
      assertEquals("hunter2", network.saslPassword())
    }

    @Test
    fun testUseAutoReconnect() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setUseAutoReconnect(false)
      assertEquals(false, network.useAutoReconnect())
      network.setUseAutoReconnect(true)
      assertEquals(true, network.useAutoReconnect())
    }

    @Test
    fun testAutoReconnectInterval() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(2500u, network.autoReconnectInterval())
      network.setAutoReconnectInterval(2500u)
      assertEquals(2500u, network.autoReconnectInterval())
    }

    @Test
    fun testAutoReconnectRetries() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(7u.toUShort(), network.autoReconnectRetries())
      network.setAutoReconnectRetries(7u.toUShort())
      assertEquals(7u.toUShort(), network.autoReconnectRetries())
    }

    @Test
    fun testUnlimitedReconnectRetries() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setUnlimitedReconnectRetries(false)
      assertEquals(false, network.unlimitedReconnectRetries())
      network.setUnlimitedReconnectRetries(true)
      assertEquals(true, network.unlimitedReconnectRetries())
    }

    @Test
    fun testRejoinChannels() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setRejoinChannels(false)
      assertEquals(false, network.rejoinChannels())
      network.setRejoinChannels(true)
      assertEquals(true, network.rejoinChannels())
    }

    @Test
    fun testUseCustomMessageRate() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setUseCustomMessageRate(false)
      assertEquals(false, network.useCustomMessageRate())
      network.setUseCustomMessageRate(true)
      assertEquals(true, network.useCustomMessageRate())
    }

    @Test
    fun testMessageRateBurstSize() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(20u, network.messageRateBurstSize())
      network.setMessageRateBurstSize(20u)
      assertEquals(20u, network.messageRateBurstSize())
    }

    @Test
    fun testMessageRateDelay() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals(1200u, network.messageRateDelay())
      network.setMessageRateDelay(1200u)
      assertEquals(1200u, network.messageRateDelay())
    }

    @Test
    fun testUnlimitedMessageRate() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      network.setUnlimitedMessageRate(false)
      assertEquals(false, network.unlimitedMessageRate())
      network.setUnlimitedMessageRate(true)
      assertEquals(true, network.unlimitedMessageRate())
    }

    @Test
    fun testCodecForServer() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("UTF_8", network.codecForServer())
      network.setCodecForServer(StringSerializerUtf8.serializeRaw("UTF_8"))
      assertEquals("UTF_8", network.codecForServer())
      network.setCodecForServer(StringSerializerUtf8.serializeRaw("ISO_8859_1"))
      assertEquals("ISO_8859_1", network.codecForServer())
    }

    @Test
    fun testCodecForEncoding() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("UTF_8", network.codecForEncoding())
      network.setCodecForEncoding(StringSerializerUtf8.serializeRaw("UTF_8"))
      assertEquals("UTF_8", network.codecForEncoding())
      network.setCodecForEncoding(StringSerializerUtf8.serializeRaw("ISO_8859_1"))
      assertEquals("ISO_8859_1", network.codecForEncoding())
    }

    @Test
    fun testCodecForDecoding() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(NetworkId(random.nextInt())))

      assertNotEquals("UTF_8", network.codecForDecoding())
      network.setCodecForDecoding(StringSerializerUtf8.serializeRaw("UTF_8"))
      assertEquals("UTF_8", network.codecForDecoding())
      network.setCodecForDecoding(StringSerializerUtf8.serializeRaw("ISO_8859_1"))
      assertEquals("ISO_8859_1", network.codecForDecoding())
    }
  }

  @Nested
  inner class User {
    @Test
    fun addNew() {
      val random = Random(1337)
      val session = NetworkMockSession()
      val network = Network(session, state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircUserCount()
      assertNotEquals(0, sizeBefore)
      val userName = random.nextString()
      assertFalse(network.nicks().contains(userName))
      assertFalse(session.synchronizeCalls.contains(network.ircUser(userName) as SyncableObject?))
      network.addIrcUser(userName)
      assertEquals(sizeBefore + 1, network.ircUserCount())
      assertTrue(network.nicks().contains(userName))
      assertTrue(session.synchronizeCalls.contains(network.ircUser(userName) as SyncableObject?))
    }

    @Test
    fun addNewOffline() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircUserCount()
      assertNotEquals(0, sizeBefore)
      val userName = random.nextString()
      assertFalse(network.nicks().contains(userName))
      network.addIrcUser(userName)
      assertEquals(sizeBefore + 1, network.ircUserCount())
      assertTrue(network.nicks().contains(userName))
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val session = NetworkMockSession()
      val network = Network(session, state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      assertNotEquals(0, network.ircUserCount())
      val user = network.ircUsers().first()
      assertTrue(network.nicks().contains(user.nick()))
      assertFalse(session.synchronizeCalls.contains(network.ircUser(user.nick()) as SyncableObject?))
      assertEquals(user, network.newIrcUser(user.hostMask()))
      assertTrue(network.nicks().contains(user.nick()))
      assertFalse(session.synchronizeCalls.contains(network.ircUser(user.nick()) as SyncableObject?))
    }

    @Test
    fun addExistingOffline() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      assertNotEquals(0, network.ircUserCount())
      val user = network.ircUsers().first()
      assertTrue(network.nicks().contains(user.nick()))
      assertEquals(user, network.newIrcUser(user.hostMask()))
      assertTrue(network.nicks().contains(user.nick()))
    }

    @Test
    fun removeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircUserCount()
      assertNotEquals(0, sizeBefore)
      val user = network.ircUsers().first()
      assertTrue(network.nicks().contains(user.nick()))
      network.removeIrcUser(user)
      assertEquals(sizeBefore - 1, network.ircUserCount())
      assertFalse(network.nicks().contains(user.nick()))
    }
  }

  @Nested
  inner class Channel {
    @Test
    fun addNew() {
      val random = Random(1337)
      val session = NetworkMockSession()
      val network = Network(session, state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircChannelCount()
      assertNotEquals(0, sizeBefore)
      val channelName = random.nextString()
      assertFalse(session.synchronizeCalls.contains(network.ircChannel(channelName) as SyncableObject?))
      network.addIrcChannel(channelName)
      assertEquals(sizeBefore + 1, network.ircChannelCount())
      assertTrue(network.channels().contains(channelName))
      assertTrue(session.synchronizeCalls.contains(network.ircChannel(channelName) as SyncableObject?))
    }
    @Test
    fun addNewOffline() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.ircChannelCount()
      assertNotEquals(0, sizeBefore)
      val channelName = random.nextString()
      network.addIrcChannel(channelName)
      assertEquals(sizeBefore + 1, network.ircChannelCount())
      assertTrue(network.channels().contains(channelName))
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val session = NetworkMockSession()
      val network = Network(session, state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      assertNotEquals(0, network.ircUserCount())
      val existing = network.ircChannels().first()
      assertFalse(session.synchronizeCalls.contains(network.ircChannel(existing.name()) as SyncableObject?))
      assertEquals(existing, network.newIrcChannel(existing.name()))
      assertFalse(session.synchronizeCalls.contains(network.ircChannel(existing.name()) as SyncableObject?))
    }

    @Test
    fun addExistingOffline() {
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
      val key = random.nextString()
      val value = random.nextString()
      assertFalse(network.supports(key))
      assertNotEquals(value, network.supportValue(key))
      network.addSupport(key, value)
      assertEquals(sizeBefore + 1, network.supports().size)
      assertTrue(network.supports(key))
      assertEquals(value, network.supportValue(key))
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.supports().size
      assertNotEquals(0, sizeBefore)
      val key = network.supports().keys.first()
      val value = random.nextString()
      assertTrue(network.supports(key))
      assertNotEquals(value, network.supportValue(key))
      network.addSupport(key, value)
      assertEquals(sizeBefore, network.supports().size)
      assertTrue(network.supports(key))
      assertEquals(value, network.supportValue(key))
    }

    @Test
    fun removeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.supports().size
      assertNotEquals(0, sizeBefore)
      val key = network.supports().keys.first()
      assertTrue(network.supports(key))
      network.removeSupport(key)
      assertEquals(sizeBefore - 1, network.supports().size)
      assertFalse(network.supports(key))
      assertEquals(null, network.supportValue(key))
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
      val key = random.nextString()
      val value = random.nextString()
      assertFalse(network.capAvailable(key))
      assertNotEquals(value, network.capValue(key))
      network.addCap(key, value)
      assertEquals(sizeBefore + 1, network.caps().size)
      assertTrue(network.capAvailable(key))
      assertEquals(value, network.capValue(key))
    }

    @Test
    fun addExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.caps().size
      assertNotEquals(0, sizeBefore)
      val key = network.caps().keys.first()
      val value = random.nextString()
      assertTrue(network.capAvailable(key))
      assertNotEquals(value, network.capValue(key))
      network.addCap(key, value)
      assertEquals(sizeBefore, network.caps().size)
      assertTrue(network.capAvailable(key))
      assertEquals(value, network.capValue(key))
    }

    @Test
    fun acknowledgeNew() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.capsEnabled().size
      assertNotEquals(0, sizeBefore)
      val key = random.nextString()
      assertFalse(network.capEnabled(key))
      network.acknowledgeCap(key)
      assertEquals(sizeBefore + 1, network.capsEnabled().size)
      assertTrue(network.capEnabled(key))
    }

    @Test
    fun acknowledgeExisting() {
      val random = Random(1337)
      val network = Network(state = random.nextNetwork(networkId = NetworkId(random.nextInt())))

      val sizeBefore = network.capsEnabled().size
      assertNotEquals(0, sizeBefore)
      val key = network.capsEnabled().first()
      assertTrue(network.capEnabled(key))
      network.acknowledgeCap(key)
      assertEquals(sizeBefore, network.capsEnabled().size)
      assertTrue(network.capEnabled(key))
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

      for (c in setOf('e', 'I', 'b', 'q')) {
        assertEquals(ChannelModeType.A_CHANMODE, network.channelModeType(c))
      }
      for (c in setOf('k')) {
        assertEquals(ChannelModeType.B_CHANMODE, network.channelModeType(c))
      }
      for (c in setOf('f', 'l', 'j')) {
        assertEquals(ChannelModeType.C_CHANMODE, network.channelModeType(c))
      }
      for (c in setOf('C', 'F', 'L', 'M', 'P', 'Q', 'S', 'c', 'g', 'i', 'm', 'n', 'p', 'r', 's', 't', 'u', 'z')) {
        assertEquals(ChannelModeType.D_CHANMODE, network.channelModeType(c))
      }
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

  class NetworkMockSession : MockSession() {
    val synchronizeCalls = mutableListOf<SyncableObject>()

    override fun synchronize(it: SyncableObject) {
      synchronizeCalls.add(it)
    }
  }
}
