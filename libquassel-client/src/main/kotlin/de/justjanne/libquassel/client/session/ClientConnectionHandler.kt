/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.session

import de.justjanne.libquassel.client.util.CoroutineQueue
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.session.ConnectionHandler
import de.justjanne.libquassel.protocol.session.MessageChannel

abstract class ClientConnectionHandler : ConnectionHandler {
  protected var channel: MessageChannel? = null
  private val readyQueue = CoroutineQueue<Unit>()
  override suspend fun init(channel: MessageChannel): Boolean {
    this.channel = channel
    readyQueue.resume(Unit)
    return false
  }

  suspend fun emit(message: SignalProxyMessage) {
    if (channel == null) {
      readyQueue.wait()
    }
    channel?.emit(message)
  }

  suspend fun emit(message: HandshakeMessage) {
    if (channel == null) {
      readyQueue.wait()
    }
    channel?.emit(message)
  }
}
