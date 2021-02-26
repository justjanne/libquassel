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
    match("\$channelname", "\$channel") { source ->
      Expansion.Constant(Expansion.ConstantField.CHANNEL, source)
    },
    match("\$currentnick", "\$nick") { source ->
      Expansion.Constant(Expansion.ConstantField.NICK, source)
    },
    match("\$network") { source ->
      Expansion.Constant(Expansion.ConstantField.NETWORK, source)
    },
    match("\$0") { source ->
      Expansion.Parameter(0, null, source)
    },
    match("""\$(\d+)\.\.(\d+)""".toRegex()) { source, (_, from, to) ->
      Expansion.ParameterRange(from.toInt(), to.toInt(), source)
    },
    match("""\$(\d+)\.\.""".toRegex()) { source, (_, from) ->
      Expansion.ParameterRange(from.toInt(), null, source)
    },
    match("""\$(\d+):hostname""".toRegex()) { source, (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.HOSTNAME, source)
    },
    match("""\$(\d+):identd""".toRegex()) { source, (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.VERIFIED_IDENT, source)
    },
    match("""\$(\d+):ident""".toRegex()) { source, (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.IDENT, source)
    },
    match("""\$(\d+):account""".toRegex()) { source, (_, value) ->
      Expansion.Parameter(value.toInt(), Expansion.ParameterField.ACCOUNT, source)
    },
    match("""\$(\d+)""".toRegex()) { source, (_, value) ->
      Expansion.Parameter(value.toInt(), null, source)
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
