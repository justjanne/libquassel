/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.stubs

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.annotations.SyncedCall
import de.justjanne.libquassel.annotations.SyncedObject
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject(name = "IgnoreListManager")
interface IgnoreListManagerStub : StatefulSyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addIgnoreListItem(
    type: Int,
    ignoreRule: String?,
    isRegEx: Boolean,
    strictness: Int,
    scope: Int,
    scopeRule: String?,
    isActive: Boolean
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "addIgnoreListItem",
      qVariant(type, QtType.Int),
      qVariant(ignoreRule, QtType.QString),
      qVariant(isRegEx, QtType.Bool),
      qVariant(strictness, QtType.Int),
      qVariant(scope, QtType.Int),
      qVariant(scopeRule, QtType.QString),
      qVariant(isActive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeIgnoreListItem(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeIgnoreListItem",
      qVariant(ignoreRule, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestAddIgnoreListItem(
    type: Int,
    ignoreRule: String?,
    isRegEx: Boolean,
    strictness: Int,
    scope: Int,
    scopeRule: String?,
    isActive: Boolean
  ) {
    sync(
      target = ProtocolSide.CORE,
      "requestAddIgnoreListItem",
      qVariant(type, QtType.Int),
      qVariant(ignoreRule, QtType.QString),
      qVariant(isRegEx, QtType.Bool),
      qVariant(strictness, QtType.Int),
      qVariant(scope, QtType.Int),
      qVariant(scopeRule, QtType.QString),
      qVariant(isActive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveIgnoreListItem(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveIgnoreListItem",
      qVariant(ignoreRule, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestToggleIgnoreRule(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CORE,
      "requestToggleIgnoreRule",
      qVariant(ignoreRule, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun toggleIgnoreRule(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "requestToggleIgnoreRule",
      qVariant(ignoreRule, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
