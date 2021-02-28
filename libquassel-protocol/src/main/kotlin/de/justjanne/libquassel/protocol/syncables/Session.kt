/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.stubs.BacklogManagerStub
import de.justjanne.libquassel.protocol.syncables.stubs.IgnoreListManagerStub
import de.justjanne.libquassel.protocol.syncables.stubs.IrcListHelperStub
import de.justjanne.libquassel.protocol.syncables.stubs.RpcHandlerStub
import de.justjanne.libquassel.protocol.variant.QVariantList

interface Session : RpcHandlerStub {
  val protocolSide: ProtocolSide
  val objectRepository: ObjectRepository

  fun network(id: NetworkId): Network?
  fun identity(id: IdentityId): Identity

  fun aliasManager(): AliasManager
  fun bufferSyncer(): BufferSyncer
  fun backlogManager(): BacklogManagerStub
  fun bufferViewManager(): BufferViewManager
  fun ignoreListManager(): IgnoreListManagerStub
  fun highlightRuleManager(): HighlightRuleManager
  fun ircListHelper(): IrcListHelperStub

  fun coreInfo(): CoreInfo
  fun dccConfig(): DccConfig
  fun networkConfig(): NetworkConfig

  fun synchronize(it: SyncableObject)
  fun stopSynchronize(it: SyncableObject)

  fun sync(
    target: ProtocolSide,
    className: String,
    objectName: String,
    function: String,
    arguments: QVariantList
  ) {
    if (target != protocolSide) {
      emit(
        SignalProxyMessage.Sync(
          className,
          objectName,
          function,
          arguments
        )
      )
    }
  }

  fun rpc(
    target: ProtocolSide,
    function: String,
    arguments: QVariantList
  ) {
    if (target != protocolSide) {
      emit(
        SignalProxyMessage.Rpc(
          function,
          arguments
        )
      )
    }
  }

  fun emit(message: SignalProxyMessage)
  fun dispatch(message: SignalProxyMessage)
  fun dispatch(message: SignalProxyMessage.Sync)
  fun dispatch(message: SignalProxyMessage.Rpc)
  fun dispatch(message: SignalProxyMessage.InitRequest)
  fun dispatch(message: SignalProxyMessage.InitData)
  fun dispatch(message: SignalProxyMessage.HeartBeat)
  fun dispatch(message: SignalProxyMessage.HeartBeatReply)
}
