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

sealed class Expansion {
  data class Text(val value: String) : Expansion()
  data class ParameterRange(val from: Int, val to: Int?) : Expansion()
  data class Parameter(val index: Int, val field: ParameterField?) : Expansion()
  data class Constant(val field: ConstantField) : Expansion()

  enum class ParameterField {
    HOSTNAME,
    VERIFIED_IDENT,
    IDENT,
    ACCOUNT
  }

  enum class ConstantField {
    CHANNEL,
    NICK,
    NETWORK
  }

  companion object {
    fun parse(text: String): List<Expansion> =
      ExpansionParsingContext(text).parse()
  }
}
