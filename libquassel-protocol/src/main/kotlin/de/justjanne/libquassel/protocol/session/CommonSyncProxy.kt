/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.session

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantList

class CommonSyncProxy(
  private val protocolSide: ProtocolSide,
  private val objectRepository: ObjectRepository,
  private val proxyMessageHandler: ProxyMessageHandler
) : SyncProxy {
  override fun synchronize(syncable: SyncableStub) {
    if (objectRepository.add(syncable)) {
      proxyMessageHandler.dispatch(SignalProxyMessage.InitRequest(syncable.className, syncable.objectName))
    }
  }

  override fun stopSynchronize(syncable: SyncableStub) {
    objectRepository.remove(syncable)
  }

  override fun sync(
    target: ProtocolSide,
    className: String,
    objectName: String,
    function: String,
    arguments: QVariantList
  ) {
    if (target != protocolSide) {
      proxyMessageHandler.emit(
        SignalProxyMessage.Sync(
          className,
          objectName,
          function,
          arguments
        )
      )
    }
  }

  override fun rpc(
    target: ProtocolSide,
    function: String,
    arguments: QVariantList
  ) {
    if (target != protocolSide) {
      proxyMessageHandler.emit(
        SignalProxyMessage.Rpc(
          function,
          arguments
        )
      )
    }
  }
}
