/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import de.justjanne.libquassel.protocol.util.StateHolder
import de.justjanne.libquassel.protocol.util.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runInterruptible
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import javax.net.ssl.SSLContext

class CoroutineChannel : StateHolder<CoroutineChannelState> {
  private lateinit var channel: StreamChannel
  private val writeContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
  private val readContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

  suspend fun connect(
    address: InetSocketAddress,
    timeout: Int = 0,
    keepAlive: Boolean = false,
  ) = runInterruptible(Dispatchers.IO) {
    val socket = Socket()
    socket.keepAlive = keepAlive
    socket.connect(address, timeout)
    this.channel = StreamChannel(socket)
    state.update {
      copy(connected = true)
    }
  }

  fun enableCompression() {
    channel = channel.withCompression()
    state.update {
      copy(compression = true)
    }
  }

  suspend fun enableTLS(context: SSLContext) {
    channel = runInterruptible(writeContext) {
      channel.withTLS(context)
    }
    state.update {
      copy(tlsInfo = channel.tlsInfo())
    }
  }

  suspend fun read(buffer: ByteBuffer): Int = runInterruptible(readContext) {
    channel.read(buffer)
  }

  suspend fun write(buffer: ByteBuffer): Int = runInterruptible(writeContext) {
    channel.write(buffer)
  }

  suspend fun write(chainedBuffer: ChainedByteBuffer) {
    for (buffer in chainedBuffer.iterator()) {
      write(buffer)
    }
  }

  suspend fun flush() = runInterruptible(writeContext) {
    channel.flush()
  }

  fun close() {
    channel.close()
    state.update {
      copy(connected = false)
    }
  }

  override fun state(): CoroutineChannelState = state.value
  override fun flow(): Flow<CoroutineChannelState> = state
  private val state = MutableStateFlow(CoroutineChannelState())
}
