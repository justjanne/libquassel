/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.AliasManager
import de.justjanne.libquassel.protocol.syncables.BufferSyncer
import de.justjanne.libquassel.protocol.syncables.BufferViewManager
import de.justjanne.libquassel.protocol.syncables.CoreInfo
import de.justjanne.libquassel.protocol.syncables.DccConfig
import de.justjanne.libquassel.protocol.syncables.HighlightRuleManager
import de.justjanne.libquassel.protocol.syncables.Identity
import de.justjanne.libquassel.protocol.syncables.Network
import de.justjanne.libquassel.protocol.syncables.NetworkConfig
import de.justjanne.libquassel.protocol.syncables.ObjectRepository
import de.justjanne.libquassel.protocol.syncables.Session
import de.justjanne.libquassel.protocol.syncables.SyncableObject
import de.justjanne.libquassel.protocol.syncables.stubs.BacklogManagerStub
import de.justjanne.libquassel.protocol.syncables.stubs.IgnoreListManagerStub
import de.justjanne.libquassel.protocol.syncables.stubs.IrcListHelperStub
import de.justjanne.libquassel.protocol.variant.QVariantMap

abstract class MockSession : Session {
  override val protocolSide: ProtocolSide
    get() = TODO("Mock Implementation")
  override val objectRepository: ObjectRepository
    get() = TODO("Mock Implementation")

  override fun network(id: NetworkId): Network? = TODO("Mock Implementation")

  override fun identity(id: IdentityId): Identity = TODO("Mock Implementation")

  override fun aliasManager(): AliasManager = TODO("Mock Implementation")

  override fun bufferSyncer(): BufferSyncer = TODO("Mock Implementation")

  override fun backlogManager(): BacklogManagerStub = TODO("Mock Implementation")

  override fun bufferViewManager(): BufferViewManager = TODO("Mock Implementation")

  override fun ignoreListManager(): IgnoreListManagerStub = TODO("Mock Implementation")

  override fun highlightRuleManager(): HighlightRuleManager = TODO("Mock Implementation")

  override fun ircListHelper(): IrcListHelperStub = TODO("Mock Implementation")

  override fun coreInfo(): CoreInfo = TODO("Mock Implementation")

  override fun dccConfig(): DccConfig = TODO("Mock Implementation")

  override fun networkConfig(): NetworkConfig = TODO("Mock Implementation")

  override fun synchronize(it: SyncableObject): Unit = TODO("Mock Implementation")

  override fun stopSynchronize(it: SyncableObject): Unit = TODO("Mock Implementation")

  override fun emit(message: SignalProxyMessage): Unit = TODO("Mock Implementation")

  override fun dispatch(message: SignalProxyMessage): Unit = TODO("Mock Implementation")

  override fun dispatch(message: SignalProxyMessage.Sync): Unit = TODO("Mock Implementation")

  override fun dispatch(message: SignalProxyMessage.Rpc): Unit = TODO("Mock Implementation")

  override fun dispatch(message: SignalProxyMessage.InitRequest): Unit = TODO("Mock Implementation")

  override fun dispatch(message: SignalProxyMessage.InitData): Unit = TODO("Mock Implementation")

  override fun dispatch(message: SignalProxyMessage.HeartBeat): Unit = TODO("Mock Implementation")

  override fun dispatch(message: SignalProxyMessage.HeartBeatReply): Unit = TODO("Mock Implementation")

  override val className: String
    get() = TODO("Mock Implementation")
  override val objectName: String
    get() = TODO("Mock Implementation")
  override val initialized: Boolean
    get() = TODO("Mock Implementation")
  override val session: Session?
    get() = TODO("Mock Implementation")

  override fun fromVariantMap(properties: QVariantMap): Unit = TODO("Mock Implementation")

  override fun toVariantMap(): QVariantMap = TODO("Mock Implementation")
}
