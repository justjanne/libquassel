/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil

import de.justjanne.bitflags.of
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.features.LegacyFeature
import de.justjanne.libquassel.protocol.features.QuasselFeatureName
import de.justjanne.libquassel.protocol.models.BufferActivity
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.ConnectedClient
import de.justjanne.libquassel.protocol.models.alias.Alias
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.network.NetworkServer
import de.justjanne.libquassel.protocol.models.rules.HighlightRule
import de.justjanne.libquassel.protocol.models.rules.IgnoreRule
import de.justjanne.libquassel.protocol.syncables.common.BufferViewConfig
import de.justjanne.libquassel.protocol.syncables.common.IrcChannel
import de.justjanne.libquassel.protocol.syncables.common.IrcUser
import de.justjanne.libquassel.protocol.syncables.state.AliasManagerState
import de.justjanne.libquassel.protocol.syncables.state.BufferSyncerState
import de.justjanne.libquassel.protocol.syncables.state.BufferViewConfigState
import de.justjanne.libquassel.protocol.syncables.state.BufferViewManagerState
import de.justjanne.libquassel.protocol.syncables.state.CertManagerState
import de.justjanne.libquassel.protocol.syncables.state.CoreInfoState
import de.justjanne.libquassel.protocol.syncables.state.DccConfigState
import de.justjanne.libquassel.protocol.syncables.state.IdentityState
import de.justjanne.libquassel.protocol.syncables.state.IrcChannelState
import de.justjanne.libquassel.protocol.syncables.state.IrcUserState
import de.justjanne.libquassel.protocol.syncables.state.NetworkConfigState
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit
import java.net.InetAddress
import java.util.EnumSet
import java.util.Locale
import java.util.UUID
import kotlin.random.Random
import kotlin.random.nextUInt

fun Random.nextString(): String {
  return UUID(nextLong(), nextLong()).toString()
}

fun <T> Random.nextOf(elements: Collection<T>): T {
  val index = nextInt(elements.size)
  return elements.drop(index).first()
}

fun <T> Random.nextOf(vararg elements: T): T {
  return nextOf(elements.toList())
}

inline fun <reified T : Enum<T>> Random.nextEnum(): T {
  return nextOf(EnumSet.allOf(T::class.java).toList())
}

fun Random.nextInstant(): Instant = Instant.ofEpochMilli(nextLong())

fun Random.nextNetwork(networkId: NetworkId = NetworkId(nextInt())) = NetworkState(
  networkId = networkId,
  identity = IdentityId(nextInt()),
  myNick = nextString(),
  latency = nextInt(),
  networkName = nextString(),
  currentServer = nextString(),
  connected = nextBoolean(),
  connectionState = nextEnum(),
  ircUsers = List(nextInt(20)) {
    IrcUser(state = nextIrcUser(networkId))
  }.associateBy(IrcUser::nick).mapKeys { (key) ->
    key.lowercase(Locale.ROOT)
  },
  ircChannels = List(nextInt(20)) {
    IrcChannel(state = nextIrcChannel(networkId))
  }.associateBy(IrcChannel::name).mapKeys { (key) ->
    key.lowercase(Locale.ROOT)
  },
  supports = List(nextInt(20)) {
    nextString().uppercase(Locale.ROOT) to nextString()
  }.toMap(),
  caps = List(nextInt(20)) {
    nextString().lowercase(Locale.ROOT) to nextString()
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

fun Random.nextIrcUser(
  networkId: NetworkId = NetworkId(nextInt())
) = IrcUserState(
  network = networkId,
  nick = nextString(),
  user = nextString(),
  host = nextString(),
  realName = nextString(),
  account = nextString(),
  away = nextBoolean(),
  awayMessage = nextString(),
  idleTime = nextInstant(),
  loginTime = nextInstant(),
  server = nextString(),
  ircOperator = nextString(),
  lastAwayMessageTime = nextInstant(),
  whoisServiceReply = nextString(),
  suserHost = nextString(),
  encrypted = nextBoolean()
)

fun Random.nextIrcChannel(
  networkId: NetworkId = NetworkId(nextInt())
) = IrcChannelState(
  network = networkId,
  name = nextString(),
  topic = nextString(),
  password = nextString(),
  encrypted = nextBoolean()
)

fun Random.nextBufferViewConfig(
  bufferViewId: Int = nextInt()
) = BufferViewConfigState(
  bufferViewId = bufferViewId,
  bufferViewName = nextString(),
  networkId = NetworkId(nextInt()),
  addNewBuffersAutomatically = nextBoolean(),
  sortAlphabetically = nextBoolean(),
  hideInactiveNetworks = nextBoolean(),
  hideInactiveBuffers = nextBoolean(),
  disableDecoration = nextBoolean(),
  allowedBufferTypes = BufferType.of(
    List(nextInt(BufferType.values().size)) {
      nextEnum()
    }
  ),
  minimumActivity = nextEnum<BufferActivity>(),
  showSearch = nextBoolean(),
  buffers = List(nextInt(5, 20)) {
    BufferId(nextInt(0, 33))
  }.toSet().toList().shuffled(),
  removedBuffers = List(nextInt(5, 20)) {
    BufferId(nextInt(34, 66))
  }.toSet(),
  hiddenBuffers = List(nextInt(5, 20)) {
    BufferId(nextInt(67, 100))
  }.toSet()
)

fun Random.nextAliasManager() = AliasManagerState(
  aliases = List(nextInt(20)) {
    Alias(nextString(), nextString())
  }
)

fun Random.nextBufferViewManager() = BufferViewManagerState(
  bufferViewConfigs = List(nextInt(20)) {
    BufferViewConfig(state = BufferViewConfigState(bufferViewId = nextInt()))
  }.associateBy(BufferViewConfig::bufferViewId)
)

fun Random.nextBufferSyncer(): BufferSyncerState {
  val bufferInfos = List(nextInt(20)) { nextBufferInfo() }
  val buffers = bufferInfos.map(BufferInfo::bufferId)
  return BufferSyncerState(
    activities = buffers.associateWith {
      MessageType.of(nextUInt())
    },
    highlightCounts = buffers.associateWith { nextUInt(20u).toInt() },
    lastSeenMsg = buffers.associateWith { MsgId(nextLong()) },
    markerLines = buffers.associateWith { MsgId(nextLong()) },
    bufferInfos = bufferInfos.associateBy(BufferInfo::bufferId),
  )
}

fun Random.nextBufferInfo(
  bufferId: BufferId = BufferId(nextInt()),
  networkId: NetworkId = NetworkId(nextInt())
) = BufferInfo(
  bufferId = bufferId,
  networkId = networkId,
  type = BufferType.of(nextUInt().toUShort()),
  groupId = -1,
  bufferName = nextString()
)

fun Random.nextCertManager(identityId: IdentityId = IdentityId(nextInt())) = CertManagerState(
  identityId = identityId,
  certificatePem = nextString(),
  privateKeyPem = nextString()
)

fun Random.nextCoreInfo() = CoreInfoState(
  version = nextString(),
  versionDate = nextInstant().truncatedTo(ChronoUnit.SECONDS),
  startTime = nextInstant(),
  connectedClientCount = nextInt(),
  connectedClients = List(nextInt(20)) {
    nextConnectedClient()
  }
)

fun Random.nextConnectedClient() = ConnectedClient(
  id = nextInt(),
  remoteAddress = nextString(),
  location = nextString(),
  version = nextString(),
  versionDate = nextInstant().truncatedTo(ChronoUnit.SECONDS),
  connectedSince = nextInstant(),
  secure = nextBoolean(),
  features = nextFeatureSet()
)

fun Random.nextFeatureSet() = FeatureSet.build(
  LegacyFeature.of(nextUInt()),
  List(nextInt(20)) {
    QuasselFeatureName(nextString())
  }
)

fun Random.nextDccConfig() = DccConfigState(
  dccEnabled = nextBoolean(),
  outgoingIp = InetAddress.getByAddress(nextBytes(4)),
  ipDetectionMode = nextEnum(),
  portSelectionMode = nextEnum(),
  minPort = nextUInt().toUShort(),
  maxPort = nextUInt().toUShort(),
  chunkSize = nextInt(),
  sendTimeout = nextInt(),
  usePassiveDcc = nextBoolean(),
  useFastSend = nextBoolean()
)

fun Random.nextHighlightRule(id: Int) = HighlightRule(
  id = id,
  content = nextString(),
  isRegEx = nextBoolean(),
  isCaseSensitive = nextBoolean(),
  isEnabled = nextBoolean(),
  isInverse = nextBoolean(),
  sender = nextString(),
  channel = nextString()
)

fun Random.nextIdentity(
  identityId: IdentityId = IdentityId(nextInt())
) = IdentityState(
  identityId = identityId,
  identityName = nextString(),
  realName = nextString(),
  nicks = List(nextInt(20)) {
    nextString()
  },
  awayNick = nextString(),
  awayNickEnabled = nextBoolean(),
  awayReason = nextString(),
  awayReasonEnabled = nextBoolean(),
  autoAwayEnabled = nextBoolean(),
  autoAwayTime = nextInt(),
  autoAwayReason = nextString(),
  autoAwayReasonEnabled = nextBoolean(),
  detachAwayEnabled = nextBoolean(),
  detachAwayReason = nextString(),
  detachAwayReasonEnabled = nextBoolean(),
  ident = nextString(),
  kickReason = nextString(),
  partReason = nextString(),
  quitReason = nextString()
)

fun Random.nextIgnoreRule() = IgnoreRule(
  type = nextEnum(),
  ignoreRule = nextString(),
  isRegEx = nextBoolean(),
  strictness = nextEnum(),
  isEnabled = nextBoolean(),
  scope = nextEnum(),
  scopeRule = nextString()
)

fun Random.nextNetworkConfig() = NetworkConfigState(
  pingTimeoutEnabled = nextBoolean(),
  pingInterval = nextInt(),
  maxPingCount = nextInt(),
  autoWhoEnabled = nextBoolean(),
  autoWhoInterval = nextInt(),
  autoWhoNickLimit = nextInt(),
  autoWhoDelay = nextInt(),
  standardCtcp = nextBoolean()
)
