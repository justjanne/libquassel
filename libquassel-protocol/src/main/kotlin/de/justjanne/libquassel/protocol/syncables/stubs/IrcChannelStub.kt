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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("IrcChannel")
interface IrcChannelStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addChannelMode(mode: Char, value: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "addChannelMode",
      qVariant(mode, QtType.QChar),
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addUserMode(nick: String, mode: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "addUserMode",
      qVariant(nick, QtType.QString),
      qVariant(mode, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun joinIrcUsers(nicks: QStringList, modes: QStringList) {
    sync(
      target = ProtocolSide.CLIENT,
      "joinIrcUsers",
      qVariant(nicks, QtType.QStringList),
      qVariant(modes, QtType.QStringList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun part(nick: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "part",
      qVariant(nick, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeChannelMode(mode: Char, value: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeChannelMode",
      qVariant(mode, QtType.QChar),
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeUserMode(nick: String, mode: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeUserMode",
      qVariant(nick, QtType.QString),
      qVariant(mode, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setEncrypted(encrypted: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setEncrypted",
      qVariant(encrypted, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setPassword(password: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setPassword",
      qVariant(password, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setTopic(topic: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setTopic",
      qVariant(topic, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUserModes(nick: String, modes: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUserModes",
      qVariant(nick, QtType.QString),
      qVariant(modes, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
