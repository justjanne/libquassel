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

import de.justjanne.libquassel.protocol.models.HighlightNickType
import de.justjanne.libquassel.protocol.models.HighlightRule
import de.justjanne.libquassel.protocol.models.flags.MessageFlag
import de.justjanne.libquassel.protocol.models.flags.MessageFlags
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.util.expression.ExpressionMatch
import de.justjanne.libquassel.protocol.util.irc.IrcFormatDeserializer

data class HighlightRuleManagerState(
  val rules: List<HighlightRule> = emptyList(),
  val highlightNickType: HighlightNickType = HighlightNickType.CurrentNick,
  val highlightNickCaseSensitive: Boolean = false
) {
  fun match(
    message: String,
    sender: String,
    type: MessageTypes,
    flags: MessageFlags,
    bufferName: String,
    currentNick: String,
    identityNicks: List<String>
  ): Boolean {
    val messageContent = IrcFormatDeserializer.stripColors(message)

    if (!type.contains(MessageType.Action) &&
      !type.contains(MessageType.Notice) &&
      !type.contains(MessageType.Plain)
    ) {
      return false
    }

    if (flags.contains(MessageFlag.Self)) {
      return false
    }

    val matchingRules = rules.asSequence()
      .filter { it.isEnabled }
      .filter { it.channelMatch.match(bufferName, true) }
      .filter { it.senderMatch.match(sender, true) }
      .filter { it.contentMatch.match(messageContent, true) }
      .toList()

    if (matchingRules.any(HighlightRule::isInverse)) {
      return false
    }

    if (matchingRules.isNotEmpty()) {
      return true
    }

    val nicks = when (highlightNickType) {
      HighlightNickType.NoNick -> return false
      HighlightNickType.CurrentNick -> listOf(currentNick)
      HighlightNickType.AllNicks -> identityNicks + currentNick
    }.filter(String::isNotBlank)

    if (nicks.isNotEmpty() && ExpressionMatch(
        nicks.joinToString("\n"),
        ExpressionMatch.MatchMode.MatchMultiPhrase,
        highlightNickCaseSensitive
      ).match(messageContent)
    ) {
      return true
    }

    return false
  }

  fun indexOf(id: Int): Int = rules.indexOfFirst { it.id == id }
  fun contains(id: Int) = rules.any { it.id == id }

  fun isEmpty() = rules.isEmpty()
  fun count() = rules.size
}
