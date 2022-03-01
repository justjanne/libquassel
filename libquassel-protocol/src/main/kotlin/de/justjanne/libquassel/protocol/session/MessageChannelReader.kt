/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.session

import de.justjanne.libquassel.protocol.util.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.nio.channels.ClosedChannelException
import java.util.concurrent.Executors

class MessageChannelReader(
  private val channel: MessageChannel
) : Closeable {
  private val executor = Executors.newSingleThreadExecutor()
  private val dispatcher = executor.asCoroutineDispatcher()
  private val scope = CoroutineScope(dispatcher)
  private var job: Job? = null

  fun start() {
    job = scope.launch {
      try {
        channel.init()
        while (isActive && channel.channel.state().connected) {
          channel.read()
        }
      } catch (e: ClosedChannelException) {
        logger.info { "Channel closed" }
        close()
      }
    }
  }

  override fun close() {
    channel.close()
    runBlocking { job?.cancelAndJoin() }
    scope.cancel()
    dispatcher.cancel()
    executor.shutdown()
  }

  companion object {
    private val logger = LoggerFactory.getLogger(MessageChannelReader::class.java)
  }
}
