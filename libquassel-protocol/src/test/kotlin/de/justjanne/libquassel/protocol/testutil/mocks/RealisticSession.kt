/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil.mocks

import de.justjanne.bitflags.of
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.network.ChannelModes
import de.justjanne.libquassel.protocol.models.network.ConnectionState
import de.justjanne.libquassel.protocol.models.network.NetworkServer
import de.justjanne.libquassel.protocol.models.network.PortDefaults
import de.justjanne.libquassel.protocol.syncables.common.BufferSyncer
import de.justjanne.libquassel.protocol.syncables.common.IrcChannel
import de.justjanne.libquassel.protocol.syncables.common.IrcUser
import de.justjanne.libquassel.protocol.syncables.common.Network
import de.justjanne.libquassel.protocol.syncables.state.BufferSyncerState
import de.justjanne.libquassel.protocol.syncables.state.IrcChannelState
import de.justjanne.libquassel.protocol.syncables.state.IrcUserState
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.util.irc.IrcCaseMapper

class RealisticSession : EmptySession() {
  private val networks = setOf(
    Network(
      this,
      NetworkState(
        networkId = NetworkId(1),
        networkName = "FreeNode",
        currentServer = "tepper.freenode.net",
        connected = true,
        connectionState = ConnectionState.Initialized,
        myNick = "justJanne",
        latency = 48,
        identity = IdentityId(1),
        serverList = listOf(
          NetworkServer(
            "irc.freenode.net",
            PortDefaults.PORT_SSL.port,
            useSsl = true
          ),
          NetworkServer(
            "chat.freenode.net",
            PortDefaults.PORT_SSL.port,
            useSsl = true
          )
        ),
        supports = mapOf(
          "CHANTYPES" to "#",
          "EXCEPTS" to null,
          "INVEX" to null,
          "CHANMODES" to "eIbq,k,flj,CFLMPQScgimnprstz",
          "CHANLIMIT" to "#:120",
          "PREFIX" to "(ov)@+",
          "MAXLIST" to "bqeI:100",
          "MODES" to "4",
          "NETWORK" to "freenode",
          "STATUSMSG" to "@+",
          "CALLERID" to "g",
          "CASEMAPPING" to "rfc1459",
          "CHARSET" to "ascii",
          "NICKLEN" to "16",
          "CHANNELLEN" to "50",
          "TOPICLEN" to "390",
          "DEAF" to "D",
          "FNC" to null,
          "TARGMAX" to "NAMES:1,LIST:1,KICK:1,WHOIS:1,PRIVMSG:4,NOTICE:4,ACCEPT:,MONITOR:",
          "EXTBAN" to "$,ajrxz",
          "CLIENTVER" to "3.0",
          "ETRACE" to null,
          "KNOCK" to null,
          "WHOX" to null,
          "CPRIVMSG" to null,
          "CNOTICE" to null,
          "SAFELIST" to null,
          "ELIST" to "CTU",
        ),
        caps = mapOf(
          "account-notify" to null,
          "sasl" to null,
          "identify-msg" to null,
          "multi-prefix" to null,
          "extended-join" to null
        ),
        capsEnabled = setOf(
          "sasl",
          "account-notify",
          "extended-join",
          "multi-prefix"
        ),
        ircUsers = setOf(
          IrcUser(
            this,
            IrcUserState(
              network = NetworkId(1),
              nick = "justJanne",
              user = "kuschku",
              host = "kuschku.de",
              realName = "Janne Mareike Koschinski <janne@kuschku.de>",
              account = "justJanne",
              server = "tepper.freenode.net"
            )
          ),
          IrcUser(
            this,
            IrcUserState(
              network = NetworkId(1),
              nick = "digitalcircuit",
              user = "~quassel",
              host = "2605:6000:1518:830d:ec4:7aff:fe6b:c6b0",
              realName = "Shane <avatar@mg.zorro.casa>",
              account = "digitalcircuit",
              server = "wolfe.freenode.net"
            )
          ),
          IrcUser(
            this,
            IrcUserState(
              network = NetworkId(1),
              nick = "Sput",
              user = "~sputnick",
              host = "quassel/developer/sput",
              realName = "Sputnick -- http://quassel-irc.org",
              account = "Sput",
              server = "niven.freenode.net"
            )
          )
        ).associateBy { IrcCaseMapper["rfc1459"].toLowerCase(it.nick()) },
        ircChannels = setOf(
          IrcChannel(
            this,
            IrcChannelState(
              network = NetworkId(1),
              name = "#quassel-test",
              topic = "Quassel testing channel",
              channelModes = ChannelModes(
                d = setOf('n', 't', 'c')
              ),
              userModes = mapOf(
                "justjanne" to emptySet(),
                "digitalcircuit" to emptySet(),
                "Sput" to emptySet(),
              )
            )
          )
        ).associateBy(IrcChannel::name)
      )
    )
  ).associateBy(Network::networkId)

  private val buffers = setOf(
    BufferInfo(
      bufferId = BufferId(1),
      networkId = NetworkId(1),
      bufferName = "FreeNode",
      type = BufferType.of(BufferType.Status)
    ),
    BufferInfo(
      bufferId = BufferId(2),
      networkId = NetworkId(1),
      bufferName = "#quassel-test",
      type = BufferType.of(BufferType.Channel)
    ),
    BufferInfo(
      bufferId = BufferId(3),
      networkId = NetworkId(1),
      bufferName = "digitalcircuit",
      type = BufferType.of(BufferType.Query)
    ),
    BufferInfo(
      bufferId = BufferId(4),
      networkId = NetworkId(1),
      bufferName = "ChanServ",
      type = BufferType.of(BufferType.Query)
    ),
  ).associateBy(BufferInfo::bufferId)

  override fun network(id: NetworkId): Network? = networks[id]

  override val bufferSyncer = BufferSyncer(
    this,
    BufferSyncerState(
      bufferInfos = buffers
    )
  )
}
