/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.io

import de.justjanne.libquassel.client.util.TlsInfo
import java.io.Flushable
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel
import java.nio.channels.InterruptibleChannel
import java.util.zip.InflaterInputStream
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket

class StreamChannel constructor(
  private val socket: Socket,
  private val inputStream: InputStream = socket.getInputStream(),
  private val outputStream: OutputStream = socket.getOutputStream(),
) : Flushable by outputStream, ByteChannel, InterruptibleChannel {
  private val input = ReadableWrappedChannel(inputStream)
  private val output = WritableWrappedChannel(outputStream)

  fun tlsInfo(): TlsInfo? {
    val sslSocket = socket as? SSLSocket ?: return null
    return TlsInfo.ofSession(sslSocket.session)
  }

  fun withCompression(): StreamChannel {
    return StreamChannel(
      socket,
      InflaterInputStream(inputStream),
      FixedDeflaterOutputStream(outputStream),
    )
  }

  fun withTLS(
    context: SSLContext,
  ): StreamChannel {
    val sslSocket = context.socketFactory.createSocket(
      this.socket,
      this.socket.inetAddress.hostAddress,
      this.socket.port,
      true
    ) as SSLSocket
    sslSocket.useClientMode = true
    sslSocket.startHandshake()
    return StreamChannel(sslSocket)
  }

  override fun close() {
    input.close()
    output.close()
    socket.close()
  }

  override fun isOpen(): Boolean {
    return !socket.isClosed
  }

  override fun read(dst: ByteBuffer): Int {
    return input.read(dst)
  }

  override fun write(src: ByteBuffer): Int {
    return output.write(src)
  }
}
