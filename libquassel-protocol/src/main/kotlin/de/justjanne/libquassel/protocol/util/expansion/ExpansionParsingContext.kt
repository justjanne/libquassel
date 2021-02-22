/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.expansion

import de.justjanne.libquassel.protocol.util.ParsingContext
import java.util.function.Supplier

internal class ExpansionParsingContext(
  text: String
) : ParsingContext<Expansion>(text) {
  override val matchers: List<Supplier<Expansion?>> = listOf(
    match("\$channelname", "\$channel") {
      Expansion.Constant(Expansion.ConstantField.CHANNEL)
    },
    match("\$currentnick", "\$nick") {
      Expansion.Constant(Expansion.ConstantField.NICK)
    },
    match("\$network") {
      Expansion.Constant(Expansion.ConstantField.NETWORK)
    },
    match("\$0") {
      Expansion.Parameter(0, null)
    },
    match("""\$(\d+)\.\.(\d+)""".toRegex()) { (_, from, to) ->
      Expansion.ParameterRange(from.toInt(), to.toInt())
    },
    match("""\$(\d+)\.\.""".toRegex()) { (_, from) ->
      Expansion.ParameterRange(from.toInt(), null)
    },
    match("""\$(\d+):hostname""".toRegex()) { (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.HOSTNAME)
    },
    match("""\$(\d+):identd""".toRegex()) { (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.VERIFIED_IDENT)
    },
    match("""\$(\d+):ident""".toRegex()) { (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.IDENT)
    },
    match("""\$(\d+):account""".toRegex()) { (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.ACCOUNT)
    },
    match("""\$(\d+)""".toRegex()) { (_, value) ->
      Expansion.Parameter(value.toInt(), null)
    },
    Supplier {
      val end = text.indexOf('$', startIndex = position).let {
        if (it >= 0) it
        else text.length
      }
      if (position < end) {
        val start = position
        position = end
        val value = text.substring(start, end)
        return@Supplier Expansion.Text(value)
      }
      return@Supplier null
    }
  )
}
