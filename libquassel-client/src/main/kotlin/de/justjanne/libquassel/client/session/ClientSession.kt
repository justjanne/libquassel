/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.client.session

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.client.syncables.ClientBacklogManager
import de.justjanne.libquassel.protocol.connection.ProtocolFeatures
import de.justjanne.libquassel.protocol.connection.ProtocolMeta
import de.justjanne.libquassel.protocol.features.QuasselFeature
import de.justjanne.libquassel.protocol.io.CoroutineChannel
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.session.CommonSyncProxy
import de.justjanne.libquassel.protocol.session.MessageChannel
import de.justjanne.libquassel.protocol.session.MessageChannelReadThread
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.HeartBeatHandler
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.common.AliasManager
import de.justjanne.libquassel.protocol.syncables.common.BufferSyncer
import de.justjanne.libquassel.protocol.syncables.common.BufferViewManager
import de.justjanne.libquassel.protocol.syncables.common.CertManager
import de.justjanne.libquassel.protocol.syncables.common.CoreInfo
import de.justjanne.libquassel.protocol.syncables.common.DccConfig
import de.justjanne.libquassel.protocol.syncables.common.HighlightRuleManager
import de.justjanne.libquassel.protocol.syncables.common.Identity
import de.justjanne.libquassel.protocol.syncables.common.IgnoreListManager
import de.justjanne.libquassel.protocol.syncables.common.IrcListHelper
import de.justjanne.libquassel.protocol.syncables.common.Network
import de.justjanne.libquassel.protocol.syncables.common.NetworkConfig
import de.justjanne.libquassel.protocol.syncables.state.CertManagerState
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.util.StateHolder
import de.justjanne.libquassel.protocol.util.log.info
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.slf4j.LoggerFactory
import javax.net.ssl.SSLContext

class ClientSession(
  connection: CoroutineChannel,
  protocolFeatures: ProtocolFeatures,
  protocols: List<ProtocolMeta>,
  sslContext: SSLContext
) : Session, StateHolder<ClientSessionState> {
  override val side = ProtocolSide.CLIENT

  override val rpcHandler = ClientRpcHandler(this)
  override val heartBeatHandler = HeartBeatHandler()
  override val objectRepository = ObjectRepository()
  val handshakeHandler = ClientHandshakeHandler(this)
  val baseInitHandler = BaseInitHandler(this)
  private val proxyMessageHandler = ClientProxyMessageHandler(
    heartBeatHandler,
    objectRepository,
    rpcHandler,
    baseInitHandler
  )
  override val proxy = CommonSyncProxy(
    ProtocolSide.CLIENT,
    objectRepository,
    proxyMessageHandler
  )
  private val magicHandler = ClientMagicHandler(protocolFeatures, protocols, sslContext)
  private val messageChannel = MessageChannel(connection)

  init {
    messageChannel.register(magicHandler)
    messageChannel.register(handshakeHandler)
    messageChannel.register(proxyMessageHandler)
    MessageChannelReadThread(messageChannel).start()
  }

  override fun init(
    identityInfo: List<QVariantMap>,
    bufferInfos: List<BufferInfo>,
    networkIds: List<NetworkId>
  ) {
    logger.info {
      "Client session initialized: networks = $networkIds, buffers = $bufferInfos, identities = $identityInfo"
    }

    bufferSyncer.initializeBufferInfos(bufferInfos)

    val networks = networkIds.map {
      Network(this@ClientSession, NetworkState(networkId = it))
    }
    val identities = identityInfo.map {
      Identity(this@ClientSession).apply {
        fromVariantMap(it)
      }
    }

    state.update {
      copy(
        networks = networks.associateBy(Network::networkId),
        identities = identities.associateBy(Identity::id),
      )
    }

    for (network in networks) {
      baseInitHandler.sync(network)
    }
    for (identity in identities) {
      baseInitHandler.sync(identity)
      baseInitHandler.sync(CertManager(this, CertManagerState(identityId = identity.id())))
    }
    baseInitHandler.sync(state().aliasManager)
    baseInitHandler.sync(state().bufferSyncer)
    baseInitHandler.sync(state().bufferViewManager)
    baseInitHandler.sync(state().coreInfo)
    if (messageChannel.negotiatedFeatures.hasFeature(QuasselFeature.DccFileTransfer)) {
      baseInitHandler.sync(state().dccConfig)
    }
    baseInitHandler.sync(state().ignoreListManager)
    if (messageChannel.negotiatedFeatures.hasFeature(QuasselFeature.CoreSideHighlights)) {
      baseInitHandler.sync(state().highlightRuleManager)
    }
    baseInitHandler.sync(state().ircListHelper)
    baseInitHandler.sync(state().networkConfig)
    baseInitHandler.sync(state().backlogManager)
  }

  override fun network(id: NetworkId) = state().networks[id]
  override fun addNetwork(id: NetworkId) {
    val network = Network(this, state = NetworkState(id))
    proxy.synchronize(network)
    state.update {
      copy(networks = networks + Pair(id, network))
    }
  }

  override fun removeNetwork(id: NetworkId) {
    val network = network(id) ?: return
    proxy.stopSynchronize(network)
    state.update {
      copy(networks = networks - id)
    }
  }

  override fun networks() = state().networks.values.toSet()

  override fun identity(id: IdentityId) = state().identities[id]

  override fun addIdentity(properties: QVariantMap) {
    val identity = Identity(this)
    identity.fromVariantMap(properties)
    proxy.synchronize(identity)
    state.update {
      copy(identities = identities + Pair(identity.id(), identity))
    }
  }

  override fun removeIdentity(id: IdentityId) {
    val identity = identity(id) ?: return
    proxy.stopSynchronize(identity)
    state.update {
      copy(identities = identities - id)
    }
  }

  override fun identities() = state().identities.values.toSet()

  override fun certManager(id: IdentityId) = state().certManagers[id]

  override fun certManagers() = state().certManagers.values.toSet()

  override fun rename(className: String, oldName: String, newName: String) {
    rpcHandler.objectRenamed(
      StringSerializerUtf8.serializeRaw(className),
      oldName,
      newName
    )
  }

  override val aliasManager get() = state().aliasManager

  override val backlogManager get() = state().backlogManager

  override val bufferSyncer get() = state().bufferSyncer

  override val bufferViewManager get() = state().bufferViewManager

  override val highlightRuleManager get() = state().highlightRuleManager

  override val ignoreListManager get() = state().ignoreListManager

  override val ircListHelper get() = state().ircListHelper

  override val coreInfo get() = state().coreInfo

  override val dccConfig get() = state().dccConfig

  override val networkConfig get() = state().networkConfig

  override fun state(): ClientSessionState = state.value
  override fun flow(): Flow<ClientSessionState> = state
  private val state = MutableStateFlow(
    ClientSessionState(
      networks = mapOf(),
      identities = mapOf(),
      certManagers = mapOf(),
      aliasManager = AliasManager(this),
      backlogManager = ClientBacklogManager(this),
      bufferSyncer = BufferSyncer(this),
      bufferViewManager = BufferViewManager(this),
      highlightRuleManager = HighlightRuleManager(this),
      ignoreListManager = IgnoreListManager(this),
      ircListHelper = IrcListHelper(this),
      coreInfo = CoreInfo(this),
      dccConfig = DccConfig(this),
      networkConfig = NetworkConfig(this)
    )
  )

  companion object {
    private val logger = LoggerFactory.getLogger(ClientSession::class.java)
  }
}
