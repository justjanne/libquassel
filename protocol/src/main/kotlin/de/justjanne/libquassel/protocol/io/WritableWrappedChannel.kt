/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.WritableByteChannel
import java.nio.channels.spi.AbstractInterruptibleChannel

/**
 * Utility function to wrap an output stream into a writable channel
 */
class WritableWrappedChannel(
  private var backingStream: OutputStream
) : AbstractInterruptibleChannel(), WritableByteChannel {
  private val buffer = ByteBuffer.allocate(PAGE_SIZE)
  private val lock = Any()

  /**
   * Write a given byte buffer to the channel and return the amount of written
   * bytes
   */
  override fun write(src: ByteBuffer): Int {
    val totalData = src.remaining()
    var remainingData = totalData

    synchronized(lock) {
      while (remainingData > 0) {
        // Data to be written, always the minimum of available data and the page size
        val writtenData = remainingData.coerceAtMost(PAGE_SIZE)

        // Set new bufferId info
        buffer.clear()
        buffer.limit(writtenData)
        // Read data into bufferId
        buffer.put(src)

        try {
          // begin blocking operation, this handles interruption etc. properly
          begin()
          // Write data to backing stream
          backingStream.write(buffer.array(), 0, writtenData)
        } finally {
          // end blocking operation, this handles interruption etc. properly
          end(writtenData > 0)
        }

        // add written amount to total
        remainingData -= writtenData
      }

      return (totalData - remainingData)
    }
  }

  /**
   * Close the underlying stream
   */
  override fun implCloseChannel() = backingStream.close()

  companion object {
    private const val PAGE_SIZE = 8192
  }
}
