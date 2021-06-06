/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import de.justjanne.libquassel.protocol.util.withFlip
import java.nio.ByteBuffer

/**
 * Copy data from one buffer to another
 *
 * Starts at the current position of each buffer, copies until either buffer
 * is at its limit or the desired amount is reached.
 *
 * @param from source buffer to copy from
 * @param to target buffer to copy to
 * @param desiredAmount maximum amount to copy
 */
fun copyData(from: ByteBuffer, to: ByteBuffer, desiredAmount: Int) {
  val limit = from.limit()
  val availableAmount = minOf(from.remaining(), to.remaining())
  val amount = minOf(availableAmount, desiredAmount)
  from.limit(from.position() + amount)
  to.put(from)
  from.limit(limit)
}

/**
 * Copy data from a buffer
 *
 * Starts at the current position of the buffer, copies until either the buffer
 * is at its limit or the desired amount is reached.
 *
 * @param from source buffer to copy from
 * @param desiredAmount maximum amount to copy
 * @return buffer of the copied data
 */
fun copyData(from: ByteBuffer, desiredAmount: Int): ByteBuffer {
  val to = ByteBuffer.allocate(minOf(from.remaining(), desiredAmount))
  copyData(from, to, desiredAmount)
  return to.withFlip()
}

/**
 * Utility function to determine whether a buffer can be read from/written to.
 */
fun ByteBuffer?.isEmpty() = this == null || !this.hasRemaining()

private val alphabet = charArrayOf(
  '0', '1', '2', '3', '4', '5', '6', '7',
  '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
)

/**
 * Utility function to generate a hexdump of a buffer
 */
fun ByteBuffer.contentToString(): String {
  val position = position()
  val limit = limit()
  val result = StringBuilder()
  while (hasRemaining()) {
    val byte = get()
    val upperNibble = byte.toInt() shr 4
    val lowerNibble = byte.toInt() % 16
    result.append(alphabet[(upperNibble + 16) % 16])
    result.append(alphabet[(lowerNibble + 16) % 16])
  }
  limit(limit)
  position(position)
  return result.toString()
}
