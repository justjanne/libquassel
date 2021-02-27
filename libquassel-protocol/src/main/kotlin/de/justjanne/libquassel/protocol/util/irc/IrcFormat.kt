/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.irc

sealed class IrcFormat {
  object Italic : IrcFormat()

  object Underline : IrcFormat()

  object Strikethrough : IrcFormat()

  object Monospace : IrcFormat()

  object Bold : IrcFormat()

  data class HexColor(
    val foreground: UInt,
    val background: UInt
  ) : IrcFormat()

  data class IrcColor(
    val foreground: UByte,
    val background: UByte
  ) : IrcFormat() {
    fun copySwapped() = copy(foreground = background, background = foreground)
  }
}
