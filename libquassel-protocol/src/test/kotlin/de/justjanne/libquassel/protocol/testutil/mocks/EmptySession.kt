/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil.mocks

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
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
import de.justjanne.libquassel.protocol.variant.QVariantMap

open class EmptySession : Session {
  override val protocolSide = ProtocolSide.CLIENT
  override val rpcHandler = RpcHandler(this)
  override val objectRepository = ObjectRepository()

  override fun network(id: NetworkId): Network? = null
  override fun addNetwork(id: NetworkId) = Unit
  override fun removeNetwork(id: NetworkId) = Unit
  override fun identity(id: IdentityId): Identity? = null
  override fun addIdentity(properties: QVariantMap) = Unit
  override fun removeIdentity(id: IdentityId) = Unit

  override val aliasManager = AliasManager(this)
  override val bufferSyncer = BufferSyncer(this)
  override val backlogManager = BacklogManager(this)
  override val bufferViewManager = BufferViewManager(this)
  override val ignoreListManager = IgnoreListManager(this)
  override val highlightRuleManager = HighlightRuleManager(this)
  override val ircListHelper = IrcListHelper(this)
  override val coreInfo = CoreInfo(this)
  override val dccConfig = DccConfig(this)
  override val networkConfig = NetworkConfig(this)

  override fun synchronize(syncable: SyncableStub) = Unit
  override fun stopSynchronize(syncable: SyncableStub) = Unit
  override fun emit(message: SignalProxyMessage) = Unit
  override fun dispatch(message: SignalProxyMessage) = Unit
}
