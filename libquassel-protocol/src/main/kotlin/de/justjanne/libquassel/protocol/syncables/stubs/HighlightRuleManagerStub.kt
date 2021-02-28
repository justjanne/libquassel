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
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("HighlightRuleManager")
interface HighlightRuleManagerStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveHighlightRule",
      qVariant(highlightRule, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeHighlightRule",
      qVariant(highlightRule, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestToggleHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestToggleHighlightRule",
      qVariant(highlightRule, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun toggleHighlightRule(highlightRule: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "toggleHighlightRule",
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
      qVariant(id, QtType.Int),
      qVariant(name, QtType.QString),
      qVariant(isRegEx, QtType.Bool),
      qVariant(isCaseSensitive, QtType.Bool),
      qVariant(isEnabled, QtType.Bool),
      qVariant(isInverse, QtType.Bool),
      qVariant(sender, QtType.QString),
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
      qVariant(id, QtType.Int),
      qVariant(name, QtType.QString),
      qVariant(isRegEx, QtType.Bool),
      qVariant(isCaseSensitive, QtType.Bool),
      qVariant(isEnabled, QtType.Bool),
      qVariant(isInverse, QtType.Bool),
      qVariant(sender, QtType.QString),
      qVariant(chanName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetHighlightNick(highlightNick: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetHighlightNick",
      qVariant(highlightNick, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHighlightNick(highlightNick: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHighlightNick",
      qVariant(highlightNick, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetNicksCaseSensitive(nicksCaseSensitive: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetNicksCaseSensitive",
      qVariant(nicksCaseSensitive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNicksCaseSensitive(nicksCaseSensitive: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNicksCaseSensitive",
      qVariant(nicksCaseSensitive, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
