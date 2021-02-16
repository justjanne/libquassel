/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset

/**
 * Class encapsulating encoding/decoding for a certain charset
 */
class StringEncoder(charset: Charset) {
  private val encoder = charset.newEncoder()
  private val decoder = charset.newDecoder()
  private val charBuffer = CharBuffer.allocate(1024)

  private fun charBuffer(length: Int): CharBuffer {
    if (length < 1024) {
      return charBuffer.clear()
    } else {
      return CharBuffer.allocate(length)
    }
  }

  private fun encodeInternal(data: CharBuffer): ByteBuffer {
    encoder.reset()
    return encoder.encode(data)
  }

  /**
   * Encode a string into a bytebuffer
   */
  fun encode(data: String?): ByteBuffer {
    if (data == null || !encoder.canEncode(data)) {
      return ByteBuffer.allocateDirect(0)
    }

    val charBuffer = charBuffer(data.length)
    charBuffer.put(data)
    charBuffer.flip()
    return encodeInternal(charBuffer)
  }

  private fun replacementChar(data: Char?): Char {
    return if (data == null || !encoder.canEncode(data)) {
      if (encoder.canEncode('\uFFFD')) {
        '\uFFFD'
      } else {
        '\u0000'
      }
    } else {
      data
    }
  }

  /**
   * Encode a single char into a bytebuffer
   */
  fun encodeChar(data: Char?): ByteBuffer {
    val charBuffer = charBuffer(1)
    charBuffer.put(replacementChar(data))
    charBuffer.flip()
    return encodeInternal(charBuffer)
  }

  private fun decodeInternal(source: ByteBuffer, length: Int): CharBuffer {
    val charBuffer = charBuffer(length)
    val oldlimit = source.limit()
    source.limit(source.position() + length)
    decoder.reset()
    decoder.decode(source, charBuffer, true).also {
      if (it.isError) {
        charBuffer.put('\uFFFD')
        source.position(source.position() + it.length())
      }
    }
    source.limit(oldlimit)
    return charBuffer.flip()
  }

  /**
   * Decode a string with known length from a bytebuffer
   */
  fun decode(source: ByteBuffer, length: Int): String {
    return decodeInternal(source, length).toString()
  }

  /**
   * Decode the full bytebuffer as string
   */
  fun decode(source: ByteBuffer): String {
    return decodeInternal(source, source.remaining()).toString()
  }

  /**
   * Decode a single char from a bytebuffer
   */
  fun decodeChar(source: ByteBuffer): Char {
    return decodeInternal(source, 2).get()
  }
}
