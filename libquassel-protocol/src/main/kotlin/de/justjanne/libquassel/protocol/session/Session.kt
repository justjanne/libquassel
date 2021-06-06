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
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
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
import de.justjanne.libquassel.protocol.variant.QVariantMap

interface Session {
  val side: ProtocolSide
  val proxy: SyncProxy
  val objectRepository: ObjectRepository

  fun init(
    identities: List<QVariantMap>,
    bufferInfos: List<BufferInfo>,
    networkIds: List<NetworkId>
  )

  fun network(id: NetworkId): Network?
  fun addNetwork(id: NetworkId)
  fun removeNetwork(id: NetworkId)

  fun identity(id: IdentityId): Identity?
  fun addIdentity(properties: QVariantMap)
  fun removeIdentity(id: IdentityId)

  fun rename(className: String, oldName: String, newName: String)

  val aliasManager: AliasManager
  val backlogManager: BacklogManager
  val bufferSyncer: BufferSyncer
  val bufferViewManager: BufferViewManager
  val highlightRuleManager: HighlightRuleManager
  val ignoreListManager: IgnoreListManager
  val ircListHelper: IrcListHelper

  val coreInfo: CoreInfo
  val dccConfig: DccConfig
  val networkConfig: NetworkConfig
}
