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
import de.justjanne.libquassel.protocol.util.irc.IrcISupport
import de.justjanne.libquassel.protocol.util.transpose
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import kotlinx.coroutines.flow.MutableStateFlow
import java.nio.ByteBuffer

class Network constructor(
  networkId: NetworkId,
  session: Session
) : SyncableObject(session, "Network"), NetworkStub {
  override fun init() {
    renameObject(state().identifier())
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

  fun networkId() = state().networkId
  fun networkName() = state().networkName
  fun isConnected() = state().connected
  fun connectionState() = state().connectionState
  fun currentServer() = state().currentServer
  fun myNick() = state().myNick
  fun latency() = state().latency
  fun identity() = state().identity
  fun nicks() = state().ircUsers.keys
  fun channels() = state().ircChannels.keys
  fun caps() = state().caps
  fun capsEnabled() = state().capsEnabled

  fun serverList() = state().serverList
  fun useRandomServer() = state().useRandomServer
  fun perform() = state().perform

  fun useAutoIdentify() = state().useAutoIdentify
  fun autoIdentifyService() = state().autoIdentifyService
  fun autoIdentifyPassword() = state().autoIdentifyPassword

  fun useSasl() = state().useSasl
  fun saslAccount() = state().saslAccount
  fun saslPassword() = state().saslPassword

  fun useAutoReconnect() = state().useAutoReconnect
  fun autoReconnectInterval() = state().autoReconnectInterval
  fun autoReconnectRetries() = state().autoReconnectRetries
  fun unlimitedReconnectRetries() = state().unlimitedReconnectRetries
  fun rejoinChannels() = state().rejoinChannels

  fun useCustomMessageRate() = state().useCustomMessageRate
  fun messageRateBurstSize() = state().messageRateBurstSize
  fun messageRateDelay() = state().messageRateDelay
  fun unlimitedMessageRate() = state().unlimitedMessageRate

  fun prefixes() = state().prefixes
  fun prefixModes() = state().prefixModes
  fun channelModes() = state().channelModes

  fun supports() = state().supports
  fun supports(key: String) = state().supports(key)
  fun supportValue(key: String) = state().supportValue(key)

  fun capAvailable(capability: String) = state().capAvailable(capability)
  fun capEnabled(capability: String) = state().capEnabled(capability)
  fun capValue(capability: String) = state().capValue(capability)
  fun skipCaps() = state().skipCaps

  fun isSaslSupportLikely(mechanism: String) = state().isSaslSupportLikely(mechanism)

  fun ircUser(nickName: String) = state().ircUser(nickName)
  fun ircUsers() = state().ircUsers.values
  fun ircUserCount() = state().ircUsers.size

  fun ircChannel(name: String) = state().ircChannel(name)
  fun ircChannels() = state().ircChannels.values
  fun ircChannelCount() = state().ircChannels.size

  fun codecForServer() = state().codecForServer
  fun codecForEncoding() = state().codecForEncoding
  fun codecForDecoding() = state().codecForDecoding

  fun caseMapper() = state().caseMapper()

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

  fun me() = state().me()
  fun isMe(user: IrcUser) = state().isMe(user)

  fun channelModeType(mode: Char) = state().channelModeType(mode)

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
    val user = state().ircUsers[oldNick]
    if (user != null) {
      state.update {
        copy(ircUsers = ircUsers - oldNick + Pair(newNick, user))
      }
    }
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

  @Suppress("NOTHING_TO_INLINE")
  inline fun state() = flow().value

  @Suppress("NOTHING_TO_INLINE")
  inline fun flow() = state

  @PublishedApi
  internal val state = MutableStateFlow(
    NetworkState(
      networkId = networkId
    )
  )
}
