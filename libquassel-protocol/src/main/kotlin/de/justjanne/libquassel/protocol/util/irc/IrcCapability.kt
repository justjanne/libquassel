/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.irc

object IrcCapability {
  const val ACCOUNT_NOTIFY = "account-notify"
  const val ACCOUNT_NOTIFY_WHOX_NUMERIC = 369
  const val AWAY_NOTIFY = "away-notify"
  const val CAP_NOTIFY = "cap-notify"
  const val CHGHOST = "chghost"
  const val EXTENDED_JOIN = "extended-join"
  const val MULTI_PREFIX = "multi-prefix"
  const val SASL = "sasl"
  const val USERHOST_IN_NAMES = "userhost-in-names"

  object Vendor {
    const val ZNC_SELF_MESSAGE = "znc.in/self-message"
  }

  object SaslMechanism {
    const val PLAIN = "PLAIN"
    const val EXTERNAL = "EXTERNAL"
  }
}
