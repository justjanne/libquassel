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

import de.justjanne.libquassel.protocol.models.ids.IdentityId

data class IdentityState(
  val identityId: IdentityId = IdentityId(-1),
  val identityName: String = "<empty>",
  val realName: String = "",
  val nicks: List<String> = listOf("quassel"),
  val awayNick: String = "",
  val awayNickEnabled: Boolean = false,
  val awayReason: String = "Gone fishing.",
  val awayReasonEnabled: Boolean = true,
  val autoAwayEnabled: Boolean = false,
  val autoAwayTime: Int = 10,
  val autoAwayReason: String = "Not here. No really. not here!",
  val autoAwayReasonEnabled: Boolean = false,
  val detachAwayEnabled: Boolean = false,
  val detachAwayReason: String = "All Quassel clients vanished from the face of the earth...",
  val detachAwayReasonEnabled: Boolean = false,
  val ident: String = "quassel",
  val kickReason: String = "Kindergarten is elsewhere!",
  val partReason: String = "http://quassel-irc.org - Chat comfortably. Anywhere.",
  val quitReason: String = "http://quassel-irc.org - Chat comfortably. Anywhere."
) {
  fun identifier() = "${identityId.id}"
}
