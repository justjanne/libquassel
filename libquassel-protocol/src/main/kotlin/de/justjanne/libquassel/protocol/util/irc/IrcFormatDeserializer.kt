/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.irc

object IrcFormatDeserializer {
  fun stripColors(
    str: String
  ) = formatString(str, false, mutableListOf())

  fun formatString(
    str: String,
    colorize: Boolean
  ): Pair<String, List<FormatInfo<IrcFormat>>> {
    val list = mutableListOf<FormatInfo<IrcFormat>>()
    val content = formatString(str, colorize, list)
    return Pair(content, list)
  }

  fun formatString(
    str: String?,
    colorize: Boolean,
    output: MutableList<FormatInfo<IrcFormat>>
  ): String {
    if (str == null) return ""

    val plainText = StringBuilder()
    var bold: FormatInfoBuilder<IrcFormat.Bold>? = null
    var italic: FormatInfoBuilder<IrcFormat.Italic>? = null
    var underline: FormatInfoBuilder<IrcFormat.Underline>? = null
    var strikethrough: FormatInfoBuilder<IrcFormat.Strikethrough>? = null
    var monospace: FormatInfoBuilder<IrcFormat.Monospace>? = null
    var color: FormatInfoBuilder<IrcFormat.IrcColor>? = null
    var hexColor: FormatInfoBuilder<IrcFormat.HexColor>? = null

    fun applyFormat(desc: FormatInfoBuilder<*>) {
      output.add(FormatInfo(desc.start..plainText.length, desc.format))
    }

    // Iterating over every character
    var normalCount = 0
    var i = 0
    while (i < str.length) {
      val character = str[i]
      when (character) {
        CODE_BOLD -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          // If there is an element on stack with the same code, close it
          bold = if (bold != null) {
            if (colorize) applyFormat(bold)
            null
            // Otherwise create a new one
          } else {
            FormatInfoBuilder(
              plainText.length,
              IrcFormat.Bold
            )
          }
        }
        CODE_ITALIC -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          // If there is an element on stack with the same code, close it
          italic = if (italic != null) {
            if (colorize) applyFormat(italic)
            null
            // Otherwise create a new one
          } else {
            FormatInfoBuilder(
              plainText.length,
              IrcFormat.Italic
            )
          }
        }
        CODE_UNDERLINE -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          // If there is an element on stack with the same code, close it
          underline = if (underline != null) {
            if (colorize) applyFormat(underline)
            null
            // Otherwise create a new one
          } else {
            FormatInfoBuilder(
              plainText.length,
              IrcFormat.Underline
            )
          }
        }
        CODE_STRIKETHROUGH -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          // If there is an element on stack with the same code, close it
          strikethrough = if (strikethrough != null) {
            if (colorize) applyFormat(strikethrough)
            null
            // Otherwise create a new one
          } else {
            FormatInfoBuilder(
              plainText.length,
              IrcFormat.Strikethrough
            )
          }
        }
        CODE_MONOSPACE -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          // If there is an element on stack with the same code, close it
          monospace = if (monospace != null) {
            if (colorize) applyFormat(monospace)
            null
            // Otherwise create a new one
          } else {
            FormatInfoBuilder(
              plainText.length,
              IrcFormat.Monospace
            )
          }
        }
        CODE_COLOR -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          val foregroundStart = i + 1
          val foregroundEnd = findEndOfNumber(str, foregroundStart)
          // If we have a foreground element
          if (foregroundEnd > foregroundStart) {
            val foreground = readNumber(str, foregroundStart, foregroundEnd)

            var background: UByte? = null
            var backgroundEnd = -1
            // If we have a background code, read it
            if (str.length > foregroundEnd && str[foregroundEnd] == ',') {
              backgroundEnd = findEndOfNumber(str, foregroundEnd + 1)
              background = readNumber(str, foregroundEnd + 1, backgroundEnd)
            }
            // If previous element was also a color element, try to reuse background
            if (color != null) {
              // Apply old format
              if (colorize) applyFormat(color)
              // Reuse old background, if possible
              if (background == null)
                background = color.format.background
            }
            // Add new format
            color = FormatInfoBuilder(
              plainText.length,
              IrcFormat.IrcColor(
                foreground ?: 0xFFu,
                background ?: 0xFFu
              )
            )

            // i points in front of the next character
            i = (if (backgroundEnd == -1) foregroundEnd else backgroundEnd) - 1

            // Otherwise assume this is a closing tag
          } else if (color != null) {
            if (colorize) applyFormat(color)
            color = null
          }
        }
        CODE_HEXCOLOR -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          val foregroundStart = i + 1
          val foregroundEnd = findEndOfHexNumber(str, foregroundStart)
          // If we have a foreground element
          if (foregroundEnd > foregroundStart) {
            val foreground = readHexNumber(str, foregroundStart, foregroundEnd)

            var background: UInt? = null
            var backgroundEnd = -1
            // If we have a background code, read it
            if (str.length > foregroundEnd && str[foregroundEnd] == ',') {
              backgroundEnd = findEndOfHexNumber(str, foregroundEnd + 1)
              background = readHexNumber(str, foregroundEnd + 1, backgroundEnd)
            }
            // If previous element was also a color element, try to reuse background
            if (hexColor != null) {
              // Apply old format
              if (colorize) applyFormat(hexColor)
              // Reuse old background, if possible
              if (background == null)
                background = hexColor.format.background
            }
            // Add new format
            hexColor = FormatInfoBuilder(
              plainText.length,
              IrcFormat.HexColor(
                foreground ?: 0xFFFFFFFFu,
                background ?: 0xFFFFFFFFu
              )
            )

            // i points in front of the next character
            i = (if (backgroundEnd == -1) foregroundEnd else backgroundEnd) - 1

            // Otherwise assume this is a closing tag
          } else if (hexColor != null) {
            if (colorize) applyFormat(hexColor)
            hexColor = null
          }
        }
        CODE_SWAP -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          // If we have a color tag before, apply it, and create a new one with swapped colors
          if (color != null) {
            if (colorize) applyFormat(color)
            color = FormatInfoBuilder(
              plainText.length, color.format.copySwapped()
            )
          }
        }
        CODE_RESET -> {
          plainText.append(str.substring(i - normalCount, i))
          normalCount = 0

          // End all formatting tags
          if (bold != null) {
            if (colorize) applyFormat(bold)
            bold = null
          }
          if (italic != null) {
            if (colorize) applyFormat(italic)
            italic = null
          }
          if (underline != null) {
            if (colorize) applyFormat(underline)
            underline = null
          }
          if (color != null) {
            if (colorize) applyFormat(color)
            color = null
          }
          if (hexColor != null) {
            if (colorize) applyFormat(hexColor)
            hexColor = null
          }
        }
        else -> {
          // Just append it, if it’s not special
          normalCount++
        }
      }
      i++
    }

    plainText.append(str.substring(str.length - normalCount, str.length))

    // End all formatting tags
    if (bold != null) {
      if (colorize) applyFormat(bold)
    }
    if (italic != null) {
      if (colorize) applyFormat(italic)
    }
    if (underline != null) {
      if (colorize) applyFormat(underline)
    }
    if (strikethrough != null) {
      if (colorize) applyFormat(strikethrough)
    }
    if (monospace != null) {
      if (colorize) applyFormat(monospace)
    }
    if (color != null) {
      if (colorize) applyFormat(color)
    }
    if (hexColor != null) {
      if (colorize) applyFormat(hexColor)
    }
    return plainText.toString()
  }

  private const val CODE_BOLD = 0x02.toChar()
  private const val CODE_COLOR = 0x03.toChar()
  private const val CODE_HEXCOLOR = 0x04.toChar()
  private const val CODE_ITALIC = 0x1D.toChar()
  private const val CODE_UNDERLINE = 0x1F.toChar()
  private const val CODE_STRIKETHROUGH = 0x1E.toChar()
  private const val CODE_MONOSPACE = 0x11.toChar()
  private const val CODE_SWAP = 0x16.toChar()
  private const val CODE_RESET = 0x0F.toChar()

  /**
   * Try to read a number from a String in specified bounds
   *
   * @param str   String to be read from
   * @param start Start index (inclusive)
   * @param end   End index (exclusive)
   * @return The byte represented by the digits read from the string
   */
  fun readNumber(str: String, start: Int, end: Int): UByte? {
    val result = str.substring(start, end)
    return if (result.isEmpty()) null
    else result.toUByteOrNull(10)
  }

  /**
   * Try to read a number from a String in specified bounds
   *
   * @param str   String to be read from
   * @param start Start index (inclusive)
   * @param end   End index (exclusive)
   * @return The byte represented by the digits read from the string
   */
  fun readHexNumber(str: String, start: Int, end: Int): UInt? {
    val result = str.substring(start, end)
    return if (result.isEmpty()) null
    else result.toUIntOrNull(16)
  }

  /**
   * @param str   String to be searched in
   * @param start Start position (inclusive)
   * @return Index of first character that is not a digit
   */
  private fun findEndOfNumber(str: String, start: Int): Int {
    val searchFrame = str.substring(start)
    var i = 0
    loop@ while (i < 2 && i < searchFrame.length) {
      when (searchFrame[i]) {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
          // Do nothing
        }
        else -> break@loop
      }
      i++
    }
    return start + i
  }

  /**
   * @param str   String to be searched in
   * @param start Start position (inclusive)
   * @return Index of first character that is not a digit
   */
  private fun findEndOfHexNumber(str: String, start: Int): Int {
    val searchFrame = str.substring(start)
    var i = 0
    loop@ while (i < 6 && i < searchFrame.length) {
      when (searchFrame[i]) {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'a', 'b',
        'c', 'd', 'e', 'f' -> {
          // Do nothing
        }
        else -> break@loop
      }
      i++
    }
    return start + i
  }
}
