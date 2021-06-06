/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.irc

import java.util.Locale

abstract class IrcCaseMapper {
  @JvmName("equalsIgnoreCaseNonNull")
  fun equalsIgnoreCase(a: String?, b: String?) =
    a == null && b == null || (a != null && b != null && equalsIgnoreCase(a, b))

  abstract fun equalsIgnoreCase(a: String, b: String): Boolean

  @JvmName("toLowerCaseNonNull")
  fun toLowerCase(value: String?): String? = value
    ?.let(this@IrcCaseMapper::toLowerCase)

  abstract fun toLowerCase(value: String): String

  @JvmName("toUpperCaseNonNull")
  fun toUpperCase(value: String?): String? = value
    ?.let(this@IrcCaseMapper::toUpperCase)

  abstract fun toUpperCase(value: String): String

  object Unicode : IrcCaseMapper() {
    override fun equalsIgnoreCase(a: String, b: String): Boolean =
      a.equals(b, ignoreCase = true)

    override fun toLowerCase(value: String): String =
      value.lowercase(Locale.ROOT)

    override fun toUpperCase(value: String): String =
      value.uppercase(Locale.ROOT)
  }

  object Rfc1459 : IrcCaseMapper() {
    override fun equalsIgnoreCase(a: String, b: String): Boolean =
      toLowerCase(a) == toLowerCase(b) || toUpperCase(a) == toUpperCase(b)

    override fun toLowerCase(value: String): String =
      value.lowercase(Locale.ROOT)
        .replace('[', '{')
        .replace(']', '}')
        .replace('\\', '|')

    override fun toUpperCase(value: String): String =
      value.uppercase(Locale.ROOT)
        .replace('{', '[')
        .replace('}', ']')
        .replace('|', '\\')
  }

  companion object {
    operator fun get(caseMapping: String?) =
      if (caseMapping.equals("rfc1459", ignoreCase = true)) Rfc1459
      else Unicode
  }
}
