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
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.session.ProxyMessageHandler
import de.justjanne.libquassel.protocol.syncables.HeartBeatHandler
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.common.RpcHandler
import de.justjanne.libquassel.protocol.syncables.invoker.Invokers

class ClientProxyMessageHandler(
  val heartBeatHandler: HeartBeatHandler,
  val objectRepository: ObjectRepository,
  val rpcHandler: RpcHandler
) : ProxyMessageHandler {
  override fun emit(message: SignalProxyMessage) {
    TODO("Not Implemented")
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
}
