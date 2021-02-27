/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

import de.justjanne.libquassel.protocol.models.ids.NetworkId
import org.threeten.bp.Instant

data class IrcUserState(
  val network: NetworkId,
  val nick: String,
  val user: String,
  val host: String,
  val realName: String = "",
  val account: String = "",
  val away: Boolean = false,
  val awayMessage: String = "",
  val idleTime: Instant = Instant.EPOCH,
  val loginTime: Instant = Instant.EPOCH,
  val server: String = "",
  val ircOperator: String = "",
  val lastAwayMessageTime: Instant = Instant.EPOCH,
  val whoisServiceReply: String = "",
  val suserHost: String = "",
  val encrypted: Boolean = false,
  val channels: Set<String> = emptySet(),
  val userModes: Set<Char> = emptySet()
) {
  fun identifier() = "${network.id}/${nick}"

  fun verifiedUser() = user.let {
    if (it.startsWith("~")) null
    else it
  }

  fun hostMask() = "${nick}!${user}@${host}"
}
