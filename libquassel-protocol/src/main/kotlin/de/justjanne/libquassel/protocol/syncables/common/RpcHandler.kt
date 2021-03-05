/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.common

import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.Session
import de.justjanne.libquassel.protocol.syncables.SyncableObject
import de.justjanne.libquassel.protocol.syncables.stubs.RpcHandlerStub
import de.justjanne.libquassel.protocol.variant.QVariantMap

open class RpcHandler(
  session: Session? = null
) : SyncableObject(session, ""), RpcHandlerStub {
  override fun bufferInfoUpdated(bufferInfo: BufferInfo) {
    session?.bufferSyncer?.bufferInfoUpdated(bufferInfo)
  }

  override fun identityCreated(identity: QVariantMap) {
    session?.addIdentity(identity)
  }

  override fun identityRemoved(identityId: IdentityId) {
    session?.removeIdentity(identityId)
  }

  override fun networkCreated(networkId: NetworkId) {
    session?.addNetwork(networkId)
  }

  override fun networkRemoved(networkId: NetworkId) {
    session?.removeNetwork(networkId)
  }
}
