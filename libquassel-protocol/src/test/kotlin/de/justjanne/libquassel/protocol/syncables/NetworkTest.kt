/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.NetworkServer
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.testutil.nextEnum
import de.justjanne.libquassel.protocol.testutil.nextIrcChannel
import de.justjanne.libquassel.protocol.testutil.nextIrcUser
import de.justjanne.libquassel.protocol.testutil.nextString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextUInt

class NetworkTest {
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
}

fun Random.nextNetwork(networkId: NetworkId) = NetworkState(
  networkId = networkId,
  identity = IdentityId(nextInt()),
  myNick = nextString(),
  latency = nextInt(),
  networkName = nextString(),
  currentServer = nextString(),
  connected = nextBoolean(),
  connectionState = nextEnum(),
  ircUsers = List(nextInt(20)) {
    nextIrcUser(networkId)
  }.associateBy(IrcUser::nick),
  ircChannels = List(nextInt(20)) {
    nextIrcChannel(networkId)
  }.associateBy(IrcChannel::name),
  supports = List(nextInt(20)) {
    nextString() to nextString()
  }.toMap(),
  caps = List(nextInt(20)) {
    nextString() to nextString()
  }.toMap(),
  capsEnabled = List(nextInt(20)) {
    nextString()
  }.toSet(),
  serverList = List(nextInt(20)) {
    nextNetworkServer()
  },
  useRandomServer = nextBoolean(),
  perform = List(nextInt(20)) {
    nextString()
  },
  useAutoIdentify = nextBoolean(),
  autoIdentifyService = nextString(),
  autoIdentifyPassword = nextString(),
  useSasl = nextBoolean(),
  saslAccount = nextString(),
  saslPassword = nextString(),
  useAutoReconnect = nextBoolean(),
  autoReconnectInterval = nextUInt(),
  autoReconnectRetries = nextUInt(UShort.MAX_VALUE.toUInt()).toUShort(),
  unlimitedReconnectRetries = nextBoolean(),
  rejoinChannels = nextBoolean(),
  useCustomMessageRate = nextBoolean(),
  messageRateBurstSize = nextUInt(),
  messageRateDelay = nextUInt(),
  codecForServer = nextString(),
  codecForEncoding = nextString(),
  codecForDecoding = nextString()
)

fun Random.nextNetworkServer() = NetworkServer(
  host = nextString(),
  port = nextUInt(),
  password = nextString(),
  useSsl = nextBoolean(),
  sslVerify = nextBoolean(),
  sslVersion = nextInt(),
  useProxy = nextBoolean(),
  proxyType = nextEnum(),
  proxyHost = nextString(),
  proxyPort = nextUInt(),
  proxyUser = nextString(),
  proxyPass = nextString()
)
