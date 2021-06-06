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
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.nio.channels.ClosedChannelException

class MessageChannelReadThread(
  val channel: MessageChannel
) : Thread("Message Channel Read Thread") {
  override fun run() {
    runBlocking {
      try {
        channel.init()
        while (channel.channel.state().connected) {
          channel.read()
        }
      } catch (e: ClosedChannelException) {
        logger.info { "Channel closed" }
      }
    }
  }

  companion object {
    private val logger = LoggerFactory.getLogger(MessageChannelReadThread::class.java)
  }
}
