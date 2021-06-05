/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.client

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.client.io.CoroutineChannel
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.session.CommonSyncProxy
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.HeartBeatHandler
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.common.AliasManager
import de.justjanne.libquassel.protocol.syncables.common.BacklogManager
import de.justjanne.libquassel.protocol.syncables.common.BufferSyncer
import de.justjanne.libquassel.protocol.syncables.common.BufferViewManager
import de.justjanne.libquassel.protocol.syncables.common.CoreInfo
import de.justjanne.libquassel.protocol.syncables.common.DccConfig
import de.justjanne.libquassel.protocol.syncables.common.HighlightRuleManager
import de.justjanne.libquassel.protocol.syncables.common.Identity
import de.justjanne.libquassel.protocol.syncables.common.IgnoreListManager
import de.justjanne.libquassel.protocol.syncables.common.IrcListHelper
import de.justjanne.libquassel.protocol.syncables.common.Network
import de.justjanne.libquassel.protocol.syncables.common.NetworkConfig
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ClientSession(
  private val connection: CoroutineChannel
) : Session {
  override val side = ProtocolSide.CLIENT

  private val rpcHandler = ClientRpcHandler(this)
  private val heartBeatHandler = HeartBeatHandler()
  override val objectRepository = ObjectRepository()
  private val proxyMessageHandler = ClientProxyMessageHandler(
    heartBeatHandler,
    objectRepository,
    rpcHandler
  )
  override val proxy = CommonSyncProxy(
    ProtocolSide.CLIENT,
    objectRepository,
    proxyMessageHandler
  )

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

  @Suppress("NOTHING_TO_INLINE")
  inline fun state(): ClientSessionState = state.value

  @Suppress("NOTHING_TO_INLINE")
  inline fun flow(): Flow<ClientSessionState> = state

  @PublishedApi
  internal val state = MutableStateFlow(
    ClientSessionState(
      networks = mapOf(),
      identities = mapOf(),
      aliasManager = AliasManager(this),
      backlogManager = BacklogManager(this),
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
}
