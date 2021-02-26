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

import de.justjanne.libquassel.protocol.models.ChannelModeType
import de.justjanne.libquassel.protocol.models.ConnectionState
import de.justjanne.libquassel.protocol.models.NetworkInfo
import de.justjanne.libquassel.protocol.models.NetworkServer
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.syncables.stubs.NetworkStub
import de.justjanne.libquassel.protocol.util.indices
import de.justjanne.libquassel.protocol.util.irc.HostmaskHelper
import de.justjanne.libquassel.protocol.util.irc.IrcCapability
import de.justjanne.libquassel.protocol.util.irc.IrcCaseMapper
import de.justjanne.libquassel.protocol.util.transpose
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import kotlinx.coroutines.flow.MutableStateFlow
import java.nio.ByteBuffer
import java.util.Locale

class Network constructor(
  networkId: NetworkId,
  session: Session
) : SyncableObject(session, "Network"), NetworkStub {
  override fun init() {
    renameObject("${networkId().id}")
    super.init()
  }

  override fun fromVariantMap(properties: QVariantMap) {
    state.update {
      copy(
        networkName = properties["networkName"].into(networkName),
        currentServer = properties["currentServer"].into(currentServer),
        myNick = properties["myNick"].into(),
        latency = properties["latency"].into(latency),
        codecForServer = StringSerializerUtf8.deserializeRaw(
          properties["codecForServer"].into<ByteBuffer>()
        ),
        codecForEncoding = StringSerializerUtf8.deserializeRaw(
          properties["codecForEncoding"].into<ByteBuffer>()
        ),
        codecForDecoding = StringSerializerUtf8.deserializeRaw(
          properties["codecForDecoding"].into<ByteBuffer>()
        ),
        identity = properties["identityId"]
          .into(identity),
        connected = properties["isConnected"]
          .into(connected),
        connectionState = ConnectionState.of(
          properties["connectionState"].into(connectionState.value)
        ) ?: ConnectionState.Disconnected,
        useRandomServer = properties["useRandomServer"]
          .into(useRandomServer),
        perform = properties["perform"]
          .into(perform),
        useAutoIdentify = properties["useAutoIdentify"]
          .into(useAutoIdentify),
        autoIdentifyService = properties["autoIdentifyService"]
          .into(autoIdentifyService),
        autoIdentifyPassword = properties["autoIdentifyPassword"]
          .into(autoIdentifyPassword),
        useSasl = properties["useSasl"]
          .into(useSasl),
        saslAccount = properties["saslAccount"]
          .into(saslAccount),
        saslPassword = properties["saslPassword"]
          .into(saslPassword),
        useAutoReconnect = properties["useAutoReconnect"]
          .into(useAutoReconnect),
        autoReconnectInterval = properties["autoReconnectInterval"]
          .into(autoReconnectInterval),
        autoReconnectRetries = properties["autoReconnectRetries"]
          .into(autoReconnectRetries),
        unlimitedReconnectRetries = properties["unlimitedReconnectRetries"]
          .into(unlimitedReconnectRetries),
        rejoinChannels = properties["rejoinChannels"]
          .into(rejoinChannels),
        useCustomMessageRate = properties["useCustomMessageRate"]
          .into(useCustomMessageRate),
        messageRateBurstSize = properties["msgRateBurstSize"]
          .into(messageRateBurstSize),
        messageRateDelay = properties["messageRateDelay"]
          .into(messageRateDelay),
        unlimitedMessageRate = properties["unlimitedMessageRate"]
          .into(unlimitedMessageRate),
        skipCaps = properties["skipCaps"]
          .into<QStringList>()
          ?.filterNotNull()
          ?.toSet()
          .orEmpty(),
        serverList = properties["ServerList"].into<QVariantList>()
          ?.mapNotNull { it.into<NetworkServer>() }
          .orEmpty(),
        supports = properties["Supports"].into<QVariantMap>()
          ?.mapValues { (_, value) -> value.into<String>() }
          .orEmpty(),
        caps = properties["Caps"].into<QVariantMap>()?.mapValues { (_, value) ->
          value.into<String>()
        }.orEmpty(),
        capsEnabled = properties["CapsEnabled"].into<QVariantList>()
          ?.mapNotNull { it.into<String>() }
          ?.toSet()
          .orEmpty(),
        ircUsers = properties["IrcUsersAndChannels"].into<QVariantMap>()
          ?.get("Users")?.into<QVariantMap>()
          ?.let {
            it.indices.map { index ->
              newIrcUser(
                properties["nick"]
                  .into<QVariantList>()
                  ?.getOrNull(index)
                  .into(""),
                properties,
                index
              )
            }
          }
          ?.associateBy { caseMapper().toLowerCase(it.nick()) }
          .orEmpty(),
        ircChannels = properties["IrcUsersAndChannels"].into<QVariantMap>()
          ?.get("Channels")?.into<QVariantMap>()
          ?.let {
            it.indices.map { index ->
              newIrcChannel(
                properties["name"]
                  .into<QVariantList>()
                  ?.getOrNull(index)
                  .into(""),
                properties,
                index
              )
            }
          }
          ?.associateBy { caseMapper().toLowerCase(it.name()) }
          .orEmpty()
      )
    }
    state.update {
      val (prefixes, prefixModes) = determinePrefixes()
      val channelModeTypes = determineChannelModeTypes()
      copy(
        prefixes = prefixes,
        prefixModes = prefixModes,
        channelModes = channelModeTypes
      )
    }
  }

  override fun toVariantMap() = mapOf(
    "networkName" to qVariant(networkName(), QtType.QString),
    "currentServer" to qVariant(currentServer(), QtType.QString),
    "myNick" to qVariant(myNick(), QtType.QString),
    "latency" to qVariant(latency(), QtType.Int),
    "codecForServer" to qVariant(
      StringSerializerUtf8.serializeRaw(codecForServer()),
      QtType.QByteArray
    ),
    "codecForEncoding" to qVariant(
      StringSerializerUtf8.serializeRaw(codecForEncoding()),
      QtType.QByteArray
    ),
    "codecForDecoding" to qVariant(
      StringSerializerUtf8.serializeRaw(codecForDecoding()),
      QtType.QByteArray
    ),
    "identityId" to qVariant(identity(), QuasselType.IdentityId),
    "isConnected" to qVariant(isConnected(), QtType.Bool),
    "connectionState" to qVariant(connectionState().value, QtType.Int),
    "useRandomServer" to qVariant(useRandomServer(), QtType.Bool),
    "perform" to qVariant(perform(), QtType.QStringList),
    "useAutoIdentify" to qVariant(useAutoIdentify(), QtType.Bool),
    "autoIdentifyService" to qVariant(autoIdentifyService(), QtType.QString),
    "autoIdentifyPassword" to qVariant(autoIdentifyPassword(), QtType.QString),
    "useSasl" to qVariant(useSasl(), QtType.Bool),
    "saslAccount" to qVariant(saslAccount(), QtType.QString),
    "saslPassword" to qVariant(saslPassword(), QtType.QString),
    "useAutoReconnect" to qVariant(useAutoReconnect(), QtType.Bool),
    "autoReconnectInterval" to qVariant(autoReconnectInterval(), QtType.UInt),
    "autoReconnectRetries" to qVariant(autoReconnectRetries(), QtType.UShort),
    "unlimitedReconnectRetries" to qVariant(unlimitedReconnectRetries(), QtType.Bool),
    "rejoinChannels" to qVariant(rejoinChannels(), QtType.Bool),
    "useCustomMessageRate" to qVariant(useCustomMessageRate(), QtType.Bool),
    "msgRateBurstSize" to qVariant(messageRateBurstSize(), QtType.UInt),
    "msgRateMessageDelay" to qVariant(messageRateDelay(), QtType.UInt),
    "unlimitedMessageRate" to qVariant(unlimitedMessageRate(), QtType.Bool),
    "Supports" to qVariant(
      supports().mapValues { (_, value) -> qVariant(value, QtType.QString) },
      QtType.QVariantMap
    ),
    "ServerList" to qVariant(
      serverList().map { qVariant(it, QuasselType.NetworkServer) },
      QtType.QVariantList
    ),
    "Caps" to qVariant(
      caps().mapValues { (_, value) -> qVariant(value, QtType.QString) },
      QtType.QVariantMap
    ),
    "CapsEnabled" to qVariant(
      capsEnabled().map { qVariant(it, QtType.QString) },
      QtType.QVariantList
    ),
    "IrcUsersAndChannels" to qVariant(
      mapOf(
        "Users" to qVariant(
          ircUsers().map { it.toVariantMap() }.transpose(),
          QtType.QVariantMap
        ),
        "Channels" to qVariant(
          ircChannels().map { it.toVariantMap() }.transpose(),
          QtType.QVariantMap
        ),
      ),
      QtType.QVariantMap
    )
  )

  fun me() = ircUser(myNick() ?: "")

  fun networkId() = state.value.id
  fun networkName() = state.value.networkName
  fun isConnected() = state.value.connected
  fun connectionState() = state.value.connectionState
  fun currentServer() = state.value.currentServer
  fun myNick() = state.value.myNick
  fun latency() = state.value.latency
  fun identity() = state.value.identity
  fun nicks() = state.value.ircUsers.keys
  fun channels() = state.value.ircChannels.keys
  fun caps() = state.value.caps
  fun capsEnabled() = state.value.capsEnabled
  fun serverList() = state.value.serverList
  fun useRandomServer() = state.value.useRandomServer
  fun perform() = state.value.perform
  fun useAutoIdentify() = state.value.useAutoIdentify
  fun autoIdentifyService() = state.value.autoIdentifyService
  fun autoIdentifyPassword() = state.value.autoIdentifyPassword
  fun useSasl() = state.value.useSasl
  fun saslAccount() = state.value.saslAccount
  fun saslPassword() = state.value.saslPassword
  fun useAutoReconnect() = state.value.useAutoReconnect
  fun autoReconnectInterval() = state.value.autoReconnectInterval
  fun autoReconnectRetries() = state.value.autoReconnectRetries
  fun unlimitedReconnectRetries() = state.value.unlimitedReconnectRetries
  fun rejoinChannels() = state.value.rejoinChannels
  fun useCustomMessageRate() = state.value.useCustomMessageRate
  fun messageRateBurstSize() = state.value.messageRateBurstSize
  fun messageRateDelay() = state.value.messageRateDelay
  fun unlimitedMessageRate() = state.value.unlimitedMessageRate
  fun prefixes() = state.value.prefixes
  fun prefixModes() = state.value.prefixModes
  fun channelModes() = state.value.channelModes
  fun supports() = state.value.supports
  fun supports(key: String) =
    state.value.supports.containsKey(key.toUpperCase(Locale.ROOT))

  fun supportValue(key: String) =
    state.value.supports[key.toUpperCase(Locale.ROOT)]

  fun capAvailable(capability: String) =
    state.value.caps.containsKey(capability.toLowerCase(Locale.ROOT))

  fun capEnabled(capability: String) =
    state.value.capsEnabled.contains(capability.toLowerCase(Locale.ROOT))

  fun capValue(capability: String) =
    state.value.caps[capability.toLowerCase(Locale.ROOT)] ?: ""

  fun skipCaps() = state.value.skipCaps

  fun isSaslSupportLikely(mechanism: String): Boolean {
    if (!capAvailable(IrcCapability.SASL)) {
      return false
    }
    val capValue = capValue(IrcCapability.SASL)
    return (capValue.isBlank() || capValue.contains(mechanism, ignoreCase = true))
  }

  fun ircUser(nickName: String) =
    state.value.ircUsers[caseMapper().toLowerCase(nickName)]

  fun ircUsers() = state.value.ircUsers.values
  fun ircUserCount() = state.value.ircUsers.size
  fun ircChannel(name: String) =
    state.value.ircChannels[caseMapper().toLowerCase(name)]

  fun ircChannels() = state.value.ircChannels.values
  fun ircChannelCount() = state.value.ircChannels.size

  fun codecForServer() = state.value.codecForServer
  fun codecForEncoding() = state.value.codecForEncoding
  fun codecForDecoding() = state.value.codecForDecoding

  fun caseMapper() = IrcCaseMapper[supportValue("CASEMAPPING")]

  fun networkInfo() = NetworkInfo(
    networkName = networkName(),
    networkId = networkId(),
    identity = identity(),
    codecForServer = codecForServer(),
    codecForEncoding = codecForEncoding(),
    codecForDecoding = codecForDecoding(),
    serverList = serverList(),
    useRandomServer = useRandomServer(),
    perform = perform(),
    useAutoIdentify = useAutoIdentify(),
    autoIdentifyService = autoIdentifyService(),
    autoIdentifyPassword = autoIdentifyPassword(),
    useSasl = useSasl(),
    saslAccount = saslAccount(),
    saslPassword = saslPassword(),
    useAutoReconnect = useAutoReconnect(),
    autoReconnectInterval = autoReconnectInterval(),
    autoReconnectRetries = autoReconnectRetries(),
    unlimitedReconnectRetries = unlimitedReconnectRetries(),
    rejoinChannels = rejoinChannels(),
    useCustomMessageRate = useCustomMessageRate(),
    messageRateBurstSize = messageRateBurstSize(),
    messageRateDelay = messageRateDelay(),
    unlimitedMessageRate = unlimitedMessageRate()
  )

  override fun addIrcUser(hostmask: String) {
    newIrcUser(hostmask)
  }

  override fun addIrcChannel(channel: String) {
    newIrcChannel(channel)
  }

  fun newIrcUser(
    hostMask: String,
    properties: QVariantMap = emptyMap(),
    index: Int? = null
  ): IrcUser {
    val nick = caseMapper().toLowerCase(HostmaskHelper.nick(hostMask))
    val ircUser = ircUser(nick)
    if (ircUser != null) {
      return ircUser
    }

    val user = IrcUser(hostMask, networkId(), session)
    user.init()
    if (properties.isNotEmpty()) {
      user.fromVariantMap(properties, index)
      user.initialized = true
    }
    session.synchronize(user)
    state.update {
      copy(ircUsers = ircUsers + Pair(nick, user))
    }
    return user
  }

  fun newIrcChannel(
    name: String,
    properties: QVariantMap = emptyMap(),
    index: Int? = null
  ): IrcChannel {
    val ircChannel = ircChannel(name)
    if (ircChannel != null) {
      return ircChannel
    }

    val channel = IrcChannel(name, networkId(), session)
    channel.init()
    if (properties.isNotEmpty()) {
      channel.fromVariantMap(properties, index)
      channel.initialized = true
      session.synchronize(channel)
      state.update {
        copy(ircChannels = ircChannels + Pair(caseMapper().toLowerCase(name), channel))
      }
    }
    return channel
  }

  fun removeIrcUser(user: IrcUser) {
    state.update {
      copy(ircUsers = ircUsers - caseMapper().toLowerCase(user.nick()))
    }
  }

  fun removeIrcChannel(channel: IrcChannel) {
    state.update {
      copy(ircChannels = ircChannels - caseMapper().toLowerCase(channel.name()))
    }
  }

  fun isMe(user: IrcUser): Boolean {
    return caseMapper().equalsIgnoreCase(user.nick(), myNick())
  }

  fun channelModeType(mode: Char): ChannelModeType? {
    return channelModes().entries.find {
      it.value.contains(mode)
    }?.key
  }

  override fun addSupport(param: String, value: String) {
    state.update {
      copy(
        supports = supports + Pair(
          caseMapper().toUpperCase(param),
          value
        )
      )
    }
    super.addSupport(param, value)
  }

  override fun removeSupport(param: String) {
    state.update {
      copy(supports = supports - caseMapper().toUpperCase(param))
    }
    super.removeSupport(param)
  }

  override fun addCap(capability: String, value: String) {
    state.update {
      copy(
        caps = caps + Pair(
          caseMapper().toLowerCase(capability),
          value
        )
      )
    }
    super.addCap(capability, value)
  }

  override fun acknowledgeCap(capability: String) {
    state.update {
      copy(capsEnabled = capsEnabled + caseMapper().toLowerCase(capability))
    }
    super.acknowledgeCap(capability)
  }

  override fun removeCap(capability: String) {
    state.update {
      copy(capsEnabled = capsEnabled - caseMapper().toLowerCase(capability))
    }
    super.removeCap(capability)
  }

  override fun clearCaps() {
    state.update {
      copy(caps = emptyMap(), capsEnabled = emptySet())
    }
    super.clearCaps()
  }

  override fun setSkipCaps(skipCaps: QStringList) {
    state.update {
      copy(skipCaps = skipCaps.filterNotNull().toSet())
    }
    super.setSkipCaps(skipCaps)
  }

  fun ircUserNickChanged(old: String, new: String) {
    val oldNick = caseMapper().toLowerCase(old)
    val newNick = caseMapper().toLowerCase(new)
    val user = state.value.ircUsers[oldNick]
    if (user != null) {
      state.update {
        copy(ircUsers = ircUsers - oldNick + Pair(newNick, user))
      }
    }
  }

  private fun determineChannelModeTypes(): Map<ChannelModeType, Set<Char>> {
    return ChannelModeType.values()
      .zip(
        supportValue("CHANMODES")
          ?.split(',', limit = ChannelModeType.values().size)
          ?.map(String::toSet)
          .orEmpty()
      )
      .toMap()
  }

  private fun determinePrefixes(): Pair<List<Char>, List<Char>> {
    val defaultPrefixes = listOf('~', '&', '@', '%', '+')
    val defaultPrefixModes = listOf('q', 'a', 'o', 'h', 'v')

    val prefix = supportValue("PREFIX")
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

  override fun setIdentity(identityId: IdentityId) {
    state.update {
      copy(identity = identity)
    }
    super.setIdentity(identityId)
  }

  override fun setMyNick(myNick: String) {
    state.update {
      copy(myNick = myNick)
    }
    super.setMyNick(myNick)
  }

  override fun setLatency(latency: Int) {
    state.update {
      copy(latency = latency)
    }
    super.setLatency(latency)
  }

  override fun setNetworkName(networkName: String) {
    state.update {
      copy(networkName = networkName)
    }
    super.setNetworkName(networkName)
  }

  override fun setCurrentServer(currentServer: String) {
    state.update {
      copy(currentServer = currentServer)
    }
    super.setCurrentServer(currentServer)
  }

  override fun setConnected(isConnected: Boolean) {
    state.update {
      if (isConnected) {
        copy(connected = true)
      } else {
        ircChannels.values.forEach(session::stopSynchronize)
        ircUsers.values.forEach(session::stopSynchronize)
        copy(
          connected = isConnected,
          myNick = "",
          currentServer = "",
          ircChannels = emptyMap(),
          ircUsers = emptyMap()
        )
      }
    }
    super.setConnected(isConnected)
  }

  override fun setConnectionState(connectionState: Int) {
    state.update {
      copy(
        connectionState = ConnectionState.of(connectionState)
          ?: ConnectionState.Disconnected
      )
    }
    super.setConnectionState(connectionState)
  }

  override fun setServerList(serverList: QVariantList) {
    state.update {
      copy(
        serverList = serverList.mapNotNull {
          it.into<NetworkServer>()
        }
      )
    }
    super.setServerList(serverList)
  }

  override fun setUseRandomServer(useRandomServer: Boolean) {
    state.update {
      copy(useRandomServer = useRandomServer)
    }
    super.setUseRandomServer(useRandomServer)
  }

  override fun setPerform(perform: QStringList) {
    state.update {
      copy(perform = perform.map { it ?: "" })
    }
    super.setPerform(perform)
  }

  override fun setUseAutoIdentify(useAutoIdentify: Boolean) {
    state.update {
      copy(useAutoIdentify = useAutoIdentify)
    }
    super.setUseAutoIdentify(useAutoIdentify)
  }

  override fun setAutoIdentifyPassword(autoIdentifyPassword: String) {
    state.update {
      copy(autoIdentifyPassword = autoIdentifyPassword)
    }
    super.setAutoIdentifyPassword(autoIdentifyPassword)
  }

  override fun setAutoIdentifyService(autoIdentifyService: String) {
    state.update {
      copy(autoIdentifyService = autoIdentifyService)
    }
    super.setAutoIdentifyService(autoIdentifyService)
  }

  override fun setUseSasl(useSasl: Boolean) {
    state.update {
      copy(useSasl = useSasl)
    }
    super.setUseSasl(useSasl)
  }

  override fun setSaslAccount(saslAccount: String) {
    state.update {
      copy(saslAccount = saslAccount)
    }
    super.setSaslAccount(saslAccount)
  }

  override fun setSaslPassword(saslPassword: String) {
    state.update {
      copy(saslPassword = saslPassword)
    }
    super.setSaslPassword(saslPassword)
  }

  override fun setUseAutoReconnect(useAutoReconnect: Boolean) {
    state.update {
      copy(useAutoReconnect = useAutoReconnect)
    }
    super.setUseAutoReconnect(useAutoReconnect)
  }

  override fun setAutoReconnectInterval(autoReconnectInterval: UInt) {
    state.update {
      copy(autoReconnectInterval = autoReconnectInterval)
    }
    super.setAutoReconnectInterval(autoReconnectInterval)
  }

  override fun setAutoReconnectRetries(autoReconnectRetries: UShort) {
    state.update {
      copy(autoReconnectRetries = autoReconnectRetries)
    }
    super.setAutoReconnectRetries(autoReconnectRetries)
  }

  override fun setUnlimitedReconnectRetries(unlimitedReconnectRetries: Boolean) {
    state.update {
      copy(unlimitedReconnectRetries = unlimitedReconnectRetries)
    }
    super.setUnlimitedReconnectRetries(unlimitedReconnectRetries)
  }

  override fun setRejoinChannels(rejoinChannels: Boolean) {
    state.update {
      copy(rejoinChannels = rejoinChannels)
    }
    super.setRejoinChannels(rejoinChannels)
  }

  override fun setUseCustomMessageRate(useCustomMessageRate: Boolean) {
    state.update {
      copy(useCustomMessageRate = useCustomMessageRate)
    }
    super.setUseCustomMessageRate(useCustomMessageRate)
  }

  override fun setMessageRateBurstSize(messageRateBurstSize: UInt) {
    state.update {
      copy(messageRateBurstSize = messageRateBurstSize)
    }
    super.setMessageRateBurstSize(messageRateBurstSize)
  }

  override fun setMessageRateDelay(messageRateDelay: UInt) {
    state.update {
      copy(messageRateDelay = messageRateDelay)
    }
    super.setMessageRateDelay(messageRateDelay)
  }

  override fun setUnlimitedMessageRate(unlimitedMessageRate: Boolean) {
    state.update {
      copy(unlimitedMessageRate = unlimitedMessageRate)
    }
    super.setUnlimitedMessageRate(unlimitedMessageRate)
  }

  override fun setCodecForServer(codecForServer: ByteBuffer) {
    state.update {
      copy(codecForServer = StringSerializerUtf8.deserializeRaw(codecForServer))
    }
    super.setCodecForServer(codecForServer)
  }

  override fun setCodecForEncoding(codecForEncoding: ByteBuffer) {
    state.update {
      copy(codecForEncoding = StringSerializerUtf8.deserializeRaw(codecForEncoding))
    }
    super.setCodecForEncoding(codecForEncoding)
  }

  override fun setCodecForDecoding(codecForDecoding: ByteBuffer) {
    state.update {
      copy(codecForDecoding = StringSerializerUtf8.deserializeRaw(codecForDecoding))
    }
    super.setCodecForDecoding(codecForDecoding)
  }

  private val state = MutableStateFlow(
    NetworkState(
      id = networkId
    )
  )
}
