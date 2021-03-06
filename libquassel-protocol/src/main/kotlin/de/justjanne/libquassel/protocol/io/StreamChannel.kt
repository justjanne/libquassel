/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.io

import de.justjanne.libquassel.protocol.util.x509.TlsInfo
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

/**
 * Channel wrapping a Socket with support for TLS and compression layers
 */
class StreamChannel constructor(
  private val socket: Socket,
  private val inputStream: InputStream = socket.getInputStream(),
  private val outputStream: OutputStream = socket.getOutputStream(),
) : Flushable by outputStream, ByteChannel, InterruptibleChannel {
  private val input = ReadableWrappedChannel(inputStream)
  private val output = WritableWrappedChannel(outputStream)

  /**
   * Return metadata about the current TLS session, if enabled
   */
  fun tlsInfo(): TlsInfo? {
    val sslSocket = socket as? SSLSocket ?: return null
    return TlsInfo.ofSession(sslSocket.session)
  }

  /**
   * Return a copy of the current channel with DEFLATE compression
   */
  fun withCompression(): StreamChannel {
    return StreamChannel(
      socket,
      InflaterInputStream(inputStream),
      FixedDeflaterOutputStream(outputStream),
    )
  }

  /**
   * Return a copy of the current channel with TLS
   */
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

  /**
   * Close the underlying streams and channels
   */
  override fun close() {
    input.close()
    output.close()
    socket.close()
  }

  /**
   * Returns whether the current channel is open, can be read from and written to
   */
  override fun isOpen(): Boolean {
    return !socket.isClosed
  }

  /**
   * Reads from the channel into a given byte buffer and returns the amount of
   * written bytes
   */
  override fun read(dst: ByteBuffer): Int {
    return input.read(dst)
  }

  /**
   * Write a given byte buffer to the channel and return the amount of written
   * bytes
   */
  override fun write(src: ByteBuffer): Int {
    return output.write(src)
  }
}
