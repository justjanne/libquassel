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
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

interface IrcChannelProtocol : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addChannelMode(mode: Char, value: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "addChannelMode",
      /**
       * Construct a QVariant from a Char
       */
      qVariant(mode, QtType.QChar),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addUserMode(nick: String?, mode: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "addUserMode",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(nick, QtType.QString),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(mode, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun joinIrcUsers(nicks: QStringList, modes: QStringList) {
    sync(
      target = ProtocolSide.CLIENT,
      "joinIrcUsers",
      /**
       * Construct a QVariant from a QStringList
       */
      qVariant(nicks, QtType.QStringList),
      /**
       * Construct a QVariant from a QStringList
       */
      qVariant(modes, QtType.QStringList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun part(nick: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "part",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(nick, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeChannelMode(mode: Char, value: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeChannelMode",
      /**
       * Construct a QVariant from a Char
       */
      qVariant(mode, QtType.QChar),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeUserMode(nick: String?, mode: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeUserMode",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(nick, QtType.QString),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(mode, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setEncrypted(encrypted: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setEncrypted",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(encrypted, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setPassword(password: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setPassword",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(password, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setTopic(topic: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setTopic",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(topic, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUserModes(nick: String?, modes: String? = null) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUserModes",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(nick, QtType.QString),
      /**
       * Construct a QVariant from a String?
       */
      qVariant(modes, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
