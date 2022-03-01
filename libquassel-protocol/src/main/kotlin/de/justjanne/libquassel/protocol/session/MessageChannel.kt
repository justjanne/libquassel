/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.session

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.io.CoroutineChannel
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.serializers.HandshakeMessageSerializer
import de.justjanne.libquassel.protocol.serializers.SignalProxyMessageSerializer
import de.justjanne.libquassel.protocol.util.log.trace
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.nio.ByteBuffer

class MessageChannel(
  val channel: CoroutineChannel
) : Closeable {
  var negotiatedFeatures = FeatureSet.none()

  private var handlers = mutableListOf<ConnectionHandler>()
  fun register(handler: ConnectionHandler) {
    handlers.add(handler)
  }

  private val sendBuffer = ThreadLocal.withInitial(::ChainedByteBuffer)
  private val sizeBuffer = ThreadLocal.withInitial { ByteBuffer.allocateDirect(4) }
  suspend fun init() {
    setupHandlers()
  }

  private suspend fun readAmount(): Int {
    val sizeBuffer = sizeBuffer.get()

    sizeBuffer.clear()
    channel.read(sizeBuffer)
    sizeBuffer.flip()
    val size = sizeBuffer.int
    sizeBuffer.clear()
    return size
  }

  suspend fun read() {
    val amount = readAmount()
    val messageBuffer = ByteBuffer.allocateDirect(minOf(amount, 65 * 1024 * 1024))
    channel.read(messageBuffer)
    messageBuffer.flip()
    dispatch(messageBuffer)
  }

  private suspend fun setupHandlers(): List<ConnectionHandler> {
    val removed = mutableListOf<ConnectionHandler>()
    while (true) {
      val handler = handlers.firstOrNull()
      logger.trace { "Setting up handler $handler" }
      if (handler?.init(this) != true) {
        break
      }
      logger.trace { "Handler $handler is done" }
      removed.add(handlers.removeFirst())
    }
    if (handlers.isEmpty()) {
      logger.trace { "All handlers done" }
      channel.close()
    }
    return removed
  }

  private suspend fun dispatch(message: ByteBuffer) {
    val handlerDone = try {
      handlers.first().read(message)
    } catch (e: Exception) {
      logger.warn("Error while handling message: ", e)
      false
    }
    if (handlerDone) {
      val removed = listOf(handlers.removeFirst()) + setupHandlers()
      for (handler in removed) {
        handler.done()
      }
    }
  }

  suspend fun emit(message: HandshakeMessage) = emit {
    logger.trace { "Writing handshake message $message" }
    HandshakeMessageSerializer.serialize(it, message, negotiatedFeatures)
  }

  suspend fun emit(message: SignalProxyMessage) = emit {
    logger.trace { "Writing signal proxy message $message" }
    SignalProxyMessageSerializer.serialize(it, message, negotiatedFeatures)
  }

  suspend fun emit(sizePrefix: Boolean = true, f: (ChainedByteBuffer) -> Unit) = coroutineScope {
    val sendBuffer = sendBuffer.get()
    val sizeBuffer = sizeBuffer.get()

    f(sendBuffer)
    if (sizePrefix) {
      sizeBuffer.clear()
      sizeBuffer.putInt(sendBuffer.size)
      sizeBuffer.flip()
      channel.write(sizeBuffer)
      sizeBuffer.clear()
    }
    channel.write(sendBuffer)
    channel.flush()
    sendBuffer.clear()
  }

  override fun close() {
    channel.close()
  }

  companion object {
    private val logger = LoggerFactory.getLogger(MessageChannel::class.java)
  }
}
