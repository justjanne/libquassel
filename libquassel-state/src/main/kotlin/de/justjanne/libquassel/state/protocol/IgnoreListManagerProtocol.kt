/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.state.protocol

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.annotations.SyncedCall
import de.justjanne.libquassel.annotations.SyncedObject
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject(name = "IgnoreListManager")
interface IgnoreListManagerProtocol : SyncableProtocol {
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
      /**
       * Construct a QVariant from a Int
       */
      qVariant(type, QtType.Int),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(ignoreRule, QtType.QString),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isRegEx, QtType.Bool),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(strictness, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(scope, QtType.Int),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(scopeRule, QtType.QString),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isActive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeIgnoreListItem(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeIgnoreListItem",
      /**
       * Construct a QVariant from a String?
       */
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
      target = ProtocolSide.CLIENT,
      "requestAddIgnoreListItem",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(type, QtType.Int),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(ignoreRule, QtType.QString),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isRegEx, QtType.Bool),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(strictness, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(scope, QtType.Int),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(scopeRule, QtType.QString),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isActive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveIgnoreListItem(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveIgnoreListItem",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(ignoreRule, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestToggleIgnoreRule(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CORE,
      "requestToggleIgnoreRule",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(ignoreRule, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun toggleIgnoreRule(ignoreRule: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "requestToggleIgnoreRule",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(ignoreRule, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
