/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.irc

object HostmaskHelper {
  fun nick(mask: String) = mask
    .substringBeforeLast('@')
    .substringBefore('!')

  fun user(mask: String) = mask
    .substringBeforeLast('@')
    .substringAfter('!', missingDelimiterValue = "")

  fun host(mask: String) = mask
    .substringAfterLast('@', missingDelimiterValue = "")

  fun split(mask: String): Triple<String, String, String> {
    val userPart = mask.substringBeforeLast('@')
    val host = mask.substringAfterLast('@', missingDelimiterValue = "")

    val user = userPart.substringAfter('!', missingDelimiterValue = "")
    val nick = userPart.substringBefore('!')

    return Triple(nick, user, host)
  }

  fun build(nick: String, user: String?, host: String?) = buildString {
    append(nick)
    if (!user.isNullOrEmpty()) {
      append("!$user")
    }
    if (!host.isNullOrEmpty()) {
      append("@$host")
    }
  }
}
