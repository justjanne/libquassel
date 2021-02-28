/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.rules

import de.justjanne.libquassel.protocol.util.expression.ExpressionMatch

data class HighlightRule(
  val id: Int,
  val contents: String,
  val isRegEx: Boolean = false,
  val isCaseSensitive: Boolean = false,
  val isEnabled: Boolean = true,
  val isInverse: Boolean = false,
  val sender: String,
  val channel: String
) {
  val contentMatch = ExpressionMatch(
    contents,
    if (isRegEx) ExpressionMatch.MatchMode.MatchRegEx
    else ExpressionMatch.MatchMode.MatchPhrase,
    isCaseSensitive
  )
  val senderMatch = ExpressionMatch(
    sender,
    if (isRegEx) ExpressionMatch.MatchMode.MatchRegEx
    else ExpressionMatch.MatchMode.MatchMultiWildcard,
    isCaseSensitive
  )
  val channelMatch = ExpressionMatch(
    channel,
    if (isRegEx) ExpressionMatch.MatchMode.MatchRegEx
    else ExpressionMatch.MatchMode.MatchMultiWildcard,
    isCaseSensitive
  )
}
