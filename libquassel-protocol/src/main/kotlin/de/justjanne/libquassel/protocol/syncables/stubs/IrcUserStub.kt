/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
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
import org.threeten.bp.temporal.Temporal

@SyncedObject("IrcUser")
interface IrcUserStub : SyncableStub {

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addUserModes(modes: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "addUserModes",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(modes, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun joinChannel(channelname: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "joinChannel",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(channelname, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun partChannel(channelname: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "partChannel",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(channelname, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun quit() {
    sync(
      target = ProtocolSide.CLIENT,
      "quit",
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeUserModes(modes: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeUserModes",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(modes, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAccount(account: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAccount",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(account, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAway(away: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAway",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(away, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAwayMessage(awayMessage: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAwayMessage",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(awayMessage, QtType.QString),
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
  fun setHost(host: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHost",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(host, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setIdleTime(idleTime: Temporal) {
    sync(
      target = ProtocolSide.CLIENT,
      "setIdleTime",
      /**
       * Construct a QVariant from a Temporal object
       */
      qVariant(idleTime, QtType.QDateTime),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setIrcOperator(ircOperator: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setIrcOperator",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(ircOperator, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setLastAwayMessage(lastAwayMessage: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setLastAwayMessage",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(lastAwayMessage, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setLastAwayMessageTime(lastAwayMessageTime: Temporal) {
    sync(
      target = ProtocolSide.CLIENT,
      "setLastAwayMessageTIme",
      /**
       * Construct a QVariant from a Temporal object
       */
      qVariant(lastAwayMessageTime, QtType.QDateTime),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setLoginTime(loginTime: Temporal) {
    sync(
      target = ProtocolSide.CLIENT,
      "setLoginTime",
      /**
       * Construct a QVariant from a Temporal object
       */
      qVariant(loginTime, QtType.QDateTime),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNick(nick: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNick",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(nick, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setRealName(realName: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setRealName",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(realName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setServer(server: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setServer",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(server, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSuserHost(suserHost: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSuserHost",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(suserHost, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUser(user: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUser",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(user, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUserModes(modes: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUserModes",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(modes, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setWhoisServiceReply(whoisServiceReply: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setWhoisServiceReply",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(whoisServiceReply, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun updateHostmask(mask: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "updateHostmask",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(mask, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
