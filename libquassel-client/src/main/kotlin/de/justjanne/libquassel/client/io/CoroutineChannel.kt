/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.io

import de.justjanne.libquassel.client.util.TlsInfo
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runInterruptible
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import javax.net.ssl.SSLContext

class CoroutineChannel {
  private lateinit var channel: StreamChannel
  private val writeContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
  private val readContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
  private val _tlsInfo = MutableStateFlow<TlsInfo?>(null)
  val tlsInfo: StateFlow<TlsInfo?> get() = _tlsInfo

  suspend fun connect(
    address: InetSocketAddress,
    timeout: Int = 0,
    keepAlive: Boolean = false,
  ) = runInterruptible(Dispatchers.IO) {
    val socket = Socket()
    socket.keepAlive = keepAlive
    socket.connect(address, timeout)
    this.channel = StreamChannel(socket)
  }

  fun enableCompression() {
    channel = channel.withCompression()
  }

  suspend fun enableTLS(context: SSLContext) {
    channel = runInterruptible(writeContext) {
      channel.withTLS(context)
    }
    _tlsInfo.emit(channel.tlsInfo())
  }

  suspend fun read(buffer: ByteBuffer): Int = runInterruptible(readContext) {
    this.channel.read(buffer)
  }

  suspend fun write(buffer: ByteBuffer): Int = runInterruptible(writeContext) {
    this.channel.write(buffer)
  }

  suspend fun write(chainedBuffer: ChainedByteBuffer) {
    for (buffer in chainedBuffer.iterator()) {
      write(buffer)
    }
  }

  suspend fun flush() = runInterruptible(writeContext) {
    this.channel.flush()
  }
}
