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

@SyncedObject("HighlightRuleManager")
interface HighlightRuleManagerProtocol : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveHighlightRule",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(highlightRule, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeHighlightRule",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(highlightRule, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestToggleHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestToggleHighlightRule",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(highlightRule, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun toggleHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "toggleHighlightRule",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(highlightRule, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestAddHighlightRule(
    id: Int,
    name: String?,
    isRegEx: Boolean,
    isCaseSensitive: Boolean,
    isEnabled: Boolean,
    isInverse: Boolean,
    sender: String?,
    chanName: String?
  ) {
    sync(
      target = ProtocolSide.CORE,
      "requestToggleHighlightRule",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(id, QtType.Int),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(name, QtType.QString),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isRegEx, QtType.Bool),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isCaseSensitive, QtType.Bool),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isEnabled, QtType.Bool),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isInverse, QtType.Bool),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(sender, QtType.QString),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(chanName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addHighlightRule(
    id: Int,
    name: String?,
    isRegEx: Boolean,
    isCaseSensitive: Boolean,
    isEnabled: Boolean,
    isInverse: Boolean,
    sender: String?,
    chanName: String?
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "addHighlightRule",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(id, QtType.Int),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(name, QtType.QString),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isRegEx, QtType.Bool),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isCaseSensitive, QtType.Bool),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isEnabled, QtType.Bool),
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isInverse, QtType.Bool),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(sender, QtType.QString),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(chanName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetHighlightNick(highlightNick: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetHighlightNick",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(highlightNick, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHighlightNick(highlightNick: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHighlightNick",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(highlightNick, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetNicksCaseSensitive(nicksCaseSensitive: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetNicksCaseSensitive",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(nicksCaseSensitive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNicksCaseSensitive(nicksCaseSensitive: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNicksCaseSensitive",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(nicksCaseSensitive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
