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
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("Identity")
interface IdentityStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoAwayEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoAwayEnabled",
      qVariant(enabled, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoAwayReason(reason: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoAwayReason",
      qVariant(reason, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoAwayReasonEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoAwayReasonEnabled",
      qVariant(enabled, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoAwayTime(time: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoAwayTime",
      qVariant(time, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAwayNick(awayNick: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAwayNick",
      qVariant(awayNick, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAwayNickEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAwayNickEnabled",
      qVariant(enabled, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAwayReason(awayReason: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAwayReason",
      qVariant(awayReason, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAwayReasonEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAwayReasonEnabled",
      qVariant(enabled, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setDetachAwayEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setDetachAwayEnabled",
      qVariant(enabled, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setDetachAwayReason(reason: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setDetachAwayReason",
      qVariant(reason, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setDetachAwayReasonEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setDetachAwayReasonEnabled",
      qVariant(enabled, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setId(id: IdentityId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setId",
      qVariant(id, QuasselType.IdentityId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setIdent(ident: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setIdent",
      qVariant(ident, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setIdentityName(name: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setIdentityName",
      qVariant(name, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setKickReason(reason: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setKickReason",
      qVariant(reason, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNicks(nicks: QStringList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNicks",
      qVariant(nicks, QtType.QStringList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setPartReason(reason: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setPartReason",
      qVariant(reason, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setQuitReason(reason: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setQuitReason",
      qVariant(reason, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setRealName(realName: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setRealName",
      qVariant(realName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
