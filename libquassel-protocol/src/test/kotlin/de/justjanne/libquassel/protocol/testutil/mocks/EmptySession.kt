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
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.HeartBeatHandler
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.common.AliasManager
import de.justjanne.libquassel.protocol.syncables.common.BacklogManager
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
import de.justjanne.libquassel.protocol.syncables.common.RpcHandler
import de.justjanne.libquassel.protocol.variant.QVariantMap

open class EmptySession : Session {
  override val heartBeatHandler = HeartBeatHandler()
  override val rpcHandler = RpcHandler(this)
  final override val side = ProtocolSide.CLIENT
  final override val objectRepository = ObjectRepository()
  override val proxy = EmptySyncProxy()

  override fun init(
    identityInfo: List<QVariantMap>,
    bufferInfos: List<BufferInfo>,
    networkIds: List<NetworkId>
  ) = Unit

  override fun network(id: NetworkId): Network? = null
  override fun addNetwork(id: NetworkId) = Unit
  override fun removeNetwork(id: NetworkId) = Unit
  override fun networks() = emptySet<Network>()

  override fun identity(id: IdentityId): Identity? = null
  override fun addIdentity(properties: QVariantMap) = Unit
  override fun removeIdentity(id: IdentityId) = Unit
  override fun identities() = emptySet<Identity>()

  override fun certManager(id: IdentityId) = null
  override fun certManagers() = emptySet<CertManager>()

  override fun rename(className: String, oldName: String, newName: String) = Unit

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
}
