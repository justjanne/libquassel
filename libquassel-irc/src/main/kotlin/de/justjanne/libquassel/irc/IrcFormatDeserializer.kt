/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.irc

import de.justjanne.libquassel.irc.extensions.collapse
import kotlin.math.min

/**
 * A helper class to turn mIRC formatted Strings into Android’s SpannableStrings with the same
 * color and format codes
 */
object IrcFormatDeserializer {

  fun parse(content: String) = sequence {
    var i = 0
    var lastProcessed = 0
    var current = IrcFormat.Style()

    suspend fun SequenceScope<IrcFormat.Span>.emit() {
      if (lastProcessed != i) {
        yield(IrcFormat.Span(content.substring(lastProcessed, i), current))
        lastProcessed = i
      }
    }

    suspend fun SequenceScope<IrcFormat.Span>.processFlag(flag: IrcFormat.Flag) {
      emit()
      current = current.flipFlag(flag)
      lastProcessed = ++i
    }

    suspend fun SequenceScope<IrcFormat.Span>.processColor(
      length: Int,
      radix: Int = 10,
      range: IntRange? = null,
      matcher: (Char) -> Boolean
    ): Pair<Int, Int?>? {
      emit()

      // Skip Color Code
      lastProcessed = ++i

      val foregroundData = content.substring(i, min(i + length, content.length))
        .takeWhile(matcher)
      val foreground = foregroundData.toIntOrNull(radix)
        ?.takeIf { range == null || it in range }
        ?: return null

      // Skip foreground
      i += foregroundData.length

      val backgroundData =
        if (i < content.length && content[i] == ',')
          content.substring(i + 1, min(i + length + 1, content.length))
            .takeWhile(matcher)
        else null
      val background = backgroundData
        ?.toIntOrNull(radix)
        ?.takeIf { range == null || it in range }

      if (background != null) {
        // Skip background and separator
        i += backgroundData.length + 1
      }

      lastProcessed = i

      return Pair(foreground, background)
    }

    while (i < content.length) {
      when (content[i]) {
        CODE_BOLD -> processFlag(IrcFormat.Flag.BOLD)
        CODE_ITALIC -> processFlag(IrcFormat.Flag.ITALIC)
        CODE_UNDERLINE -> processFlag(IrcFormat.Flag.UNDERLINE)
        CODE_STRIKETHROUGH -> processFlag(IrcFormat.Flag.STRIKETHROUGH)
        CODE_MONOSPACE -> processFlag(IrcFormat.Flag.MONOSPACE)
        CODE_SWAP, CODE_SWAP_KVIRC -> processFlag(IrcFormat.Flag.INVERSE)
        CODE_COLOR -> {
          val color = processColor(length = 2, range = 0..99) {
            it in '0'..'9'
          }

          current = if (color == null) {
            current.copy(foreground = null, background = null)
          } else {
            val (foreground, background) = color
            current.copy(
              foreground = foreground.takeUnless { it == 99 }?.let { IrcFormat.Color.Mirc(it) },
              background = if (background == null) current.background
              else background.takeUnless { it == 99 }?.let { IrcFormat.Color.Mirc(it) }
            )
          }
        }
        CODE_HEXCOLOR -> {
          val color = processColor(length = 6, radix = 16) {
            it in '0'..'9' || it in 'a'..'f' || it in 'A'..'F'
          }

          current = if (color == null) {
            current.copy(foreground = null, background = null)
          } else {
            val (foreground, background) = color
            current.copy(
              foreground = IrcFormat.Color.Hex(foreground),
              background = background?.let {
                IrcFormat.Color.Hex(it)
              } ?: current.background
            )
          }
        }
        CODE_RESET -> {
          emit()
          current = IrcFormat.Style()
          lastProcessed = ++i
        }
        else -> {
          // Regular Character
          i++
        }
      }
    }

    if (lastProcessed != content.length) {
      yield(IrcFormat.Span(content.substring(lastProcessed), current))
    }
  }.collapse { prev, current ->
    if (prev.style == current.style) prev.copy(content = prev.content + current.content)
    else null
  }

  private const val CODE_BOLD = 0x02.toChar()
  private const val CODE_COLOR = 0x03.toChar()
  private const val CODE_HEXCOLOR = 0x04.toChar()
  private const val CODE_ITALIC = 0x1D.toChar()
  private const val CODE_UNDERLINE = 0x1F.toChar()
  private const val CODE_STRIKETHROUGH = 0x1E.toChar()
  private const val CODE_MONOSPACE = 0x11.toChar()
  private const val CODE_SWAP_KVIRC = 0x12.toChar()
  private const val CODE_SWAP = 0x16.toChar()
  private const val CODE_RESET = 0x0F.toChar()
}
