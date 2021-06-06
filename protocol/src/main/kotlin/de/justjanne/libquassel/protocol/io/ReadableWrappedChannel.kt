/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel
import java.nio.channels.spi.AbstractInterruptibleChannel

/**
 * Utility function to wrap an input stream into a readable channel
 */
class ReadableWrappedChannel(
  private var backingStream: InputStream
) : AbstractInterruptibleChannel(), ReadableByteChannel {
  private val buffer = ByteBuffer.allocate(PAGE_SIZE)
  private val lock = Any()

  /**
   * Reads from the channel into a given byte buffer and returns the amount of
   * written bytes
   */
  override fun read(dst: ByteBuffer): Int {
    val totalData = dst.remaining()
    var remainingData = totalData

    // used to mark if we’ve already read any data. This is used to ensure that we only ever block
    // once for reading
    var hasRead = false

    synchronized(lock) {
      // Only read as long as we have content to read, and until we’ve blocked at most once
      while (remainingData > 0 && !(hasRead && backingStream.available() == 0)) {
        // Data to be read, always the minimum of available data and the page size
        val toReadOnce = remainingData.coerceAtMost(PAGE_SIZE)
        var readData = 0

        try {
          // begin blocking operation, this handles interruption etc. properly
          begin()
          // prepare bufferId for reading by resetting position and limit
          buffer.clear()
          // read data into bufferId
          readData = backingStream.read(buffer.array(), 0, toReadOnce)
          // accurately set bufferId info
          buffer.position(readData)
        } finally {
          // end blocking operation, this handles interruption etc. properly
          end(readData > 0)
        }

        if (readData <= 0) {
          error("[ReadableWrappedChannel] Read: $readData")
        }

        // read is negative if no data was read, in that case, terminate
        if (readData < 0)
          break

        // add read amount to total
        remainingData -= readData
        // mark that we’ve read data (to only block once)
        hasRead = true

        // flip bufferId to prepare for reading
        buffer.flip()
        dst.put(buffer)
      }
    }

    return (totalData - remainingData)
  }

  /**
   * Close the underlying stream
   */
  override fun implCloseChannel() = backingStream.close()

  companion object {
    private const val PAGE_SIZE = 8192
  }
}
