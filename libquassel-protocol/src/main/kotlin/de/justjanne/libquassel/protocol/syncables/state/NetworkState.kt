/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

import de.justjanne.libquassel.protocol.models.ChannelModeType
import de.justjanne.libquassel.protocol.models.ConnectionState
import de.justjanne.libquassel.protocol.models.NetworkServer
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.IrcChannel
import de.justjanne.libquassel.protocol.syncables.IrcUser
import de.justjanne.libquassel.protocol.util.irc.IrcCapability
import de.justjanne.libquassel.protocol.util.irc.IrcCaseMapper
import de.justjanne.libquassel.protocol.util.irc.IrcISupport
import java.util.Locale

data class NetworkState(
  val networkId: NetworkId,
  val identity: IdentityId = IdentityId(-1),
  val myNick: String? = "",
  val latency: Int = 0,
  val networkName: String = "<not initialized>",
  val currentServer: String = "",
  val connected: Boolean = false,
  val connectionState: ConnectionState = ConnectionState.Disconnected,
  val ircUsers: Map<String, IrcUser> = emptyMap(),
  val ircChannels: Map<String, IrcChannel> = emptyMap(),
  val supports: Map<String, String?> = emptyMap(),
  val caps: Map<String, String?> = emptyMap(),
  val capsEnabled: Set<String> = emptySet(),
  val skipCaps: Set<String> = emptySet(),
  val serverList: List<NetworkServer> = emptyList(),
  val useRandomServer: Boolean = false,
  val perform: List<String> = emptyList(),
  val useAutoIdentify: Boolean = false,
  val autoIdentifyService: String = "",
  val autoIdentifyPassword: String = "",
  val useSasl: Boolean = false,
  val saslAccount: String = "",
  val saslPassword: String = "",
  val useAutoReconnect: Boolean = false,
  val autoReconnectInterval: UInt = 60u,
  val autoReconnectRetries: UShort = 10u,
  val unlimitedReconnectRetries: Boolean = false,
  val rejoinChannels: Boolean = false,
  val useCustomMessageRate: Boolean = false,
  val messageRateBurstSize: UInt = 5u,
  val messageRateDelay: UInt = 2200u,
  val unlimitedMessageRate: Boolean = false,
  val codecForServer: String = "UTF_8",
  val codecForEncoding: String = "UTF_8",
  val codecForDecoding: String = "UTF_8"
) {
  val prefixes: List<Char>
  val prefixModes: List<Char>
  val channelModes: Map<ChannelModeType, Set<Char>>

  init {
    val (prefixes, prefixModes) = determinePrefixes()
    this.prefixes = prefixes
    this.prefixModes = prefixModes
    this.channelModes = determineChannelModeTypes()
  }

  fun identifier() = "${networkId.id}"

  fun caseMapper() = IrcCaseMapper[supportValue(IrcISupport.CASEMAPPING)]
  fun supports(key: String) = supports.containsKey(key.toUpperCase(Locale.ROOT))
  fun supportValue(key: String) = supports[key.toUpperCase(Locale.ROOT)]

  fun capAvailable(capability: String) = caps.containsKey(capability.toLowerCase(Locale.ROOT))
  fun capEnabled(capability: String) = capsEnabled.contains(capability.toLowerCase(Locale.ROOT))
  fun capValue(capability: String) = caps[capability.toLowerCase(Locale.ROOT)] ?: ""

  fun isSaslSupportLikely(mechanism: String): Boolean {
    if (!capAvailable(IrcCapability.SASL)) {
      return false
    }
    val capValue = capValue(IrcCapability.SASL)
    return (capValue.isBlank() || capValue.contains(mechanism, ignoreCase = true))
  }

  fun ircUser(nickName: String) = ircUsers[caseMapper().toLowerCase(nickName)]
  fun ircChannel(name: String) = ircChannels[caseMapper().toLowerCase(name)]

  fun me() = myNick?.let(::ircUser)

  fun isMe(user: IrcUser): Boolean {
    return caseMapper().equalsIgnoreCase(user.nick(), myNick)
  }

  fun channelModeType(mode: Char): ChannelModeType? {
    return channelModes.entries.find {
      it.value.contains(mode)
    }?.key
  }

  private fun determinePrefixes(): Pair<List<Char>, List<Char>> {
    val defaultPrefixes = listOf('~', '&', '@', '%', '+')
    val defaultPrefixModes = listOf('q', 'a', 'o', 'h', 'v')

    val prefix = supportValue(IrcISupport.PREFIX)
      ?: return Pair(defaultPrefixes, defaultPrefixModes)

    if (prefix.startsWith("(") && prefix.contains(")")) {
      val (prefixModes, prefixes) = prefix.substringAfter('(')
        .split(')', limit = 2)
        .map(String::toList)

      return Pair(prefixModes, prefixes)
    } else if (prefix.isBlank()) {
      return Pair(defaultPrefixes, defaultPrefixModes)
    } else if ((prefix.toSet() intersect defaultPrefixes.toSet()).isNotEmpty()) {
      val (prefixes, prefixModes) = defaultPrefixes.zip(defaultPrefixModes)
        .filter { prefix.contains(it.second) }
        .unzip()

      return Pair(prefixModes, prefixes)
    } else if ((prefix.toSet() intersect defaultPrefixModes.toSet()).isNotEmpty()) {
      val (prefixes, prefixModes) = defaultPrefixes.zip(defaultPrefixModes)
        .filter { prefix.contains(it.first) }
        .unzip()

      return Pair(prefixModes, prefixes)
    }

    return Pair(defaultPrefixes, defaultPrefixModes)
  }

  private fun determineChannelModeTypes(): Map<ChannelModeType, Set<Char>> {
    return ChannelModeType.values()
      .zip(
        supportValue(IrcISupport.CHANMODES)
          ?.split(',', limit = ChannelModeType.values().size)
          ?.map(String::toSet)
          .orEmpty()
      )
      .toMap()
  }
}
