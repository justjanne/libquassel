/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.irc

import de.justjanne.libquassel.irc.extensions.joinString

object IrcFormat {
  data class Span(
    val content: String,
    val style: Style = Style()
  ) {
    override fun toString(): String = joinString(", ", "Info(", ")") {
      append(content)
      if (style != Style()) {
        append("style=$style")
      }
    }
  }

  data class Style(
    val flags: Set<Flag> = emptySet(),
    val foreground: Color? = null,
    val background: Color? = null,
  ) {
    fun flipFlag(flag: Flag) = copy(
      flags = if (flags.contains(flag)) flags - flag else flags + flag
    )

    override fun toString(): String = joinString(", ", "Info(", ")") {
      if (flags.isNotEmpty()) {
        append("flags=$flags")
      }
      if (foreground != null) {
        append("foreground=$foreground")
      }
      if (background != null) {
        append("background=$background")
      }
    }
  }

  sealed class Color {
    data class Mirc(val index: Int) : Color() {
      override fun toString(): String = "Mirc($index)"
    }

    data class Hex(val color: Int) : Color() {
      override fun toString(): String = "Hex(#${color.toString(16)})"
    }
  }

  enum class Flag {
    BOLD,
    ITALIC,
    UNDERLINE,
    STRIKETHROUGH,
    MONOSPACE,
    INVERSE
  }
}
