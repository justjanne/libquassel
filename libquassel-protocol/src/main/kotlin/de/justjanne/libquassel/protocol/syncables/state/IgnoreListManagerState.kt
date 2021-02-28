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

import de.justjanne.bitflags.of
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.models.rules.IgnoreRule
import de.justjanne.libquassel.protocol.models.rules.IgnoreType
import de.justjanne.libquassel.protocol.models.rules.ScopeType
import de.justjanne.libquassel.protocol.models.rules.StrictnessType

data class IgnoreListManagerState(
  val rules: List<IgnoreRule> = emptyList()
) {
  fun indexOf(ignoreRule: String?): Int = rules.indexOfFirst { it.ignoreRule == ignoreRule }
  fun contains(ignoreRule: String?) = rules.any { it.ignoreRule == ignoreRule }

  fun isEmpty() = rules.isEmpty()
  fun count() = rules.size

  fun matchingRules(sender: String) = rules.filter {
    it.type == IgnoreType.SenderIgnore && it.ignoreMatch.match(sender)
  }

  fun match(
    msgContents: String,
    msgSender: String,
    msgType: MessageTypes,
    network: String,
    bufferName: String
  ): StrictnessType {
    if ((MessageType.of(MessageType.Plain, MessageType.Notice, MessageType.Action) intersect msgType).isEmpty()) {
      return StrictnessType.UnmatchedStrictness
    }

    return rules.asSequence().filter {
      it.isEnabled && it.type != IgnoreType.CtcpIgnore
    }.filter {
      it.scope == ScopeType.GlobalScope ||
        it.scope == ScopeType.NetworkScope && it.scopeMatch.match(network) ||
        it.scope == ScopeType.ChannelScope && it.scopeMatch.match(bufferName)
    }.filter {
      val content = if (it.type == IgnoreType.MessageIgnore) msgContents else msgSender
      it.ignoreMatch.match(content)
    }.map {
      it.strictness
    }.maxByOrNull {
      it.value
    } ?: StrictnessType.UnmatchedStrictness
  }
}
