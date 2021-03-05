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
import de.justjanne.libquassel.protocol.exceptions.RpcInvocationFailedException
import de.justjanne.libquassel.protocol.models.Message
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.StatusMessage
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.syncables.HeartBeatHandler
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.Session
import de.justjanne.libquassel.protocol.syncables.SyncableStub
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
import de.justjanne.libquassel.protocol.syncables.common.RpcHandler
import de.justjanne.libquassel.protocol.syncables.invoker.Invokers
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.nio.ByteBuffer

class ClientSession : Session {
  override val protocolSide = ProtocolSide.CLIENT
  override val objectRepository = ObjectRepository()
  override val rpcHandler = ClientRpcHandler(this)
  val heartBeatHandler = HeartBeatHandler()

  override fun network(id: NetworkId) = state().networks[id]
  override fun addNetwork(id: NetworkId) {
    val network = Network(this, state = NetworkState(id))
    synchronize(network)
    state.update {
      copy(networks = networks + Pair(id, network))
    }
  }

  override fun removeNetwork(id: NetworkId) {
    val network = network(id) ?: return
    stopSynchronize(network)
    state.update {
      copy(networks = networks - id)
    }
  }

  override fun identity(id: IdentityId) = state().identities[id]

  override fun addIdentity(properties: QVariantMap) {
    val identity = Identity(this)
    identity.fromVariantMap(properties)
    synchronize(identity)
    state.update {
      copy(identities = identities + Pair(identity.id(), identity))
    }
  }

  override fun removeIdentity(id: IdentityId) {
    val identity = identity(id) ?: return
    stopSynchronize(identity)
    state.update {
      copy(identities = identities - id)
    }
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

  override fun synchronize(syncable: SyncableStub) {
    if (objectRepository.add(syncable)) {
      dispatch(SignalProxyMessage.InitRequest(syncable.className, syncable.objectName))
    }
  }

  override fun stopSynchronize(syncable: SyncableStub) {
    objectRepository.remove(syncable)
  }

  override fun emit(message: SignalProxyMessage) {
    TODO("Not yet implemented")
  }

  override fun dispatch(message: SignalProxyMessage) {
    when (message) {
      is SignalProxyMessage.HeartBeat -> emit(SignalProxyMessage.HeartBeatReply(message.timestamp))
      is SignalProxyMessage.HeartBeatReply -> heartBeatHandler.recomputeLatency(message.timestamp, force = true)
      is SignalProxyMessage.InitData -> objectRepository.init(
        objectRepository.find(message.className, message.objectName) ?: return,
        message.initData
      )
      is SignalProxyMessage.InitRequest -> {
        // Ignore incoming requests, we’re a client, we shouldn’t ever receive these
      }
      is SignalProxyMessage.Rpc -> {
        val invoker = Invokers.get(ProtocolSide.CLIENT, "RpcHandler")
          ?: throw RpcInvocationFailedException.InvokerNotFoundException("RpcHandler")
        invoker.invoke(rpcHandler, message.slotName, message.params)
      }
      is SignalProxyMessage.Sync -> {
        val invoker = Invokers.get(ProtocolSide.CLIENT, message.className)
          ?: throw RpcInvocationFailedException.InvokerNotFoundException(message.className)
        val syncable = objectRepository.find(message.className, message.objectName)
          ?: throw RpcInvocationFailedException.SyncableNotFoundException(message.className, message.objectName)
        invoker.invoke(syncable, message.slotName, message.params)
      }
    }
  }

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

  class ClientRpcHandler(session: Session) : RpcHandler(session) {
    override fun objectRenamed(classname: ByteBuffer, newName: String?, oldName: String?) {
      val objectRepository = session?.objectRepository ?: return
      val className = StringSerializerUtf8.deserializeRaw(classname)
      val syncable = objectRepository.find(className, oldName ?: return)
        ?: return
      objectRepository.rename(syncable, newName ?: return)
    }

    override fun displayMsg(message: Message) {
      messages.tryEmit(message)
    }

    override fun displayStatusMsg(net: String?, msg: String?) {
      statusMessage.tryEmit(StatusMessage(net, msg ?: return))
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun messages(): Flow<Message> = messages

    @PublishedApi
    internal val messages = MutableSharedFlow<Message>()

    @Suppress("NOTHING_TO_INLINE")
    inline fun statusMessage(): StateFlow<StatusMessage?> = statusMessage

    @PublishedApi
    internal val statusMessage = MutableStateFlow<StatusMessage?>(null)
  }
}
