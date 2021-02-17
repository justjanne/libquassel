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

import de.justjanne.libquassel.protocol.util.withFlip
import java.nio.ByteBuffer
import java.util.LinkedList

/**
 * Linked List of ByteBuffers, able to be written to as if it was a single one.
 * Automatically expands if needed.
 */
class ChainedByteBuffer(
  private val chunkSize: Int = 1024,
  private val direct: Boolean = false,
  private val limit: Long = 0,
) : Iterable<ByteBuffer> {
  private val bufferList: MutableList<ByteBuffer> = LinkedList()

  private var currentIndex = 0

  /**
   * Number of bytes in the buffer
   */
  var size = 0
    private set

  private fun allocate(amount: Int): ByteBuffer {
    require(limit <= 0 || size + amount <= limit) {
      "Can not allocate $amount bytes, currently at $size, limit is $limit"
    }
    return if (direct) ByteBuffer.allocateDirect(amount)
    else ByteBuffer.allocate(amount)
  }

  private fun ensureSpace(requested: Int) {
    if (bufferList.lastOrNull()?.remaining() ?: 0 < requested) {
      bufferList.add(allocate(chunkSize))
    }
    size += requested
  }

  /**
   * Write an 8-bit signed byte to the buffer and increment its position.
   */
  fun put(value: Byte) {
    ensureSpace(1)

    bufferList.last().put(value)
  }

  /**
   * Write an 8-bit unsigned byte to the buffer and increment its position.
   */
  fun put(value: UByte) {
    ensureSpace(1)

    bufferList.last().put(value.toByte())
  }

  /**
   * Write a 16-bit signed short to the buffer and increment its position.
   */
  fun putShort(value: Short) {
    ensureSpace(2)

    bufferList.last().putShort(value)
  }

  /**
   * Write a 16-bit unsigned short to the buffer and increment its position.
   */
  fun putShort(value: UShort) {
    ensureSpace(2)

    bufferList.last().putShort(value.toShort())
  }

  /**
   * Write a 32-bit signed int to the buffer and increment its position.
   */
  fun putInt(value: Int) {
    ensureSpace(4)

    bufferList.last().putInt(value)
  }

  /**
   * Write a 32-bit unsigned int to the buffer and increment its position.
   */
  fun putInt(value: UInt) {
    ensureSpace(4)

    bufferList.last().putInt(value.toInt())
  }

  /**
   * Write a 64-bit signed long to the buffer and increment its position.
   */
  fun putLong(value: Long) {
    ensureSpace(8)

    bufferList.last().putLong(value)
  }

  /**
   * Write a 64-bit unsigned long to the buffer and increment its position.
   */
  fun putLong(value: ULong) {
    ensureSpace(8)

    bufferList.last().putLong(value.toLong())
  }

  /**
   * Write a 32-bit float to the buffer and increment its position.
   */
  fun putFloat(value: Float) {
    ensureSpace(4)

    bufferList.last().putFloat(value)
  }

  /**
   * Write a 64-bit double to the buffer and increment its position.
   */
  fun putDouble(value: Double) {
    ensureSpace(8)

    bufferList.last().putDouble(value)
  }

  /**
   * Write the full remaining content of a bytebuffer to the buffer and increment its position
   */
  fun put(value: ByteBuffer) {
    while (value.hasRemaining()) {
      val requested = minOf(value.remaining(), chunkSize)
      if (bufferList.lastOrNull()?.hasRemaining() != true) {
        ensureSpace(requested)
      } else {
        ensureSpace(minOf(bufferList.last().remaining(), requested))
      }

      copyData(value, bufferList.last(), requested)
    }
  }

  /**
   * Write the full remaining content of a bytearray to the buffer and increment its position
   */
  fun put(value: ByteArray) {
    this.put(ByteBuffer.wrap(value))
  }

  /**
   * Empty the buffer
   */
  fun clear() {
    bufferList.clear()
    size = 0
  }

  override fun iterator(): Iterator<ByteBuffer> =
    ChainedByteBufferIterator(this)

  /**
   * Convert this buffer into a bytebuffer of the same capacity and content.
   */
  fun toBuffer(): ByteBuffer {
    val byteBuffer = allocate(chunkSize * bufferList.size)
    for (buffer in iterator()) {
      byteBuffer.put(buffer)
    }
    byteBuffer.flip()
    return byteBuffer
  }

  private class ChainedByteBufferIterator(
    private val buffer: ChainedByteBuffer
  ) : Iterator<ByteBuffer> {
    private var index = 0

    override fun hasNext() =
      index < buffer.bufferList.size

    override fun next(): ByteBuffer =
      buffer.bufferList[index++].duplicate().withFlip()
  }
}
