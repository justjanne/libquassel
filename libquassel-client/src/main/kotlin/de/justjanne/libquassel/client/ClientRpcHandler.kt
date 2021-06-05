/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client

import de.justjanne.libquassel.protocol.models.Message
import de.justjanne.libquassel.protocol.models.StatusMessage
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.common.RpcHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.nio.ByteBuffer

class ClientRpcHandler(session: Session) : RpcHandler(session) {
  override fun objectRenamed(classname: ByteBuffer, newName: String?, oldName: String?) {
    val objectRepository = session?.objectRepository ?: return
    val className = StringSerializerUtf8.deserializeRaw(classname)
    val syncable = objectRepository.find(className, oldName ?: return)
      ?: return
    objectRepository.rename(syncable, newName ?: return)
  }

  override fun displayMsg(message: Message) {
    messages.tryEmit(message)
  }

  override fun displayStatusMsg(net: String?, msg: String?) {
    statusMessage.tryEmit(StatusMessage(net, msg ?: return))
  }

  @Suppress("NOTHING_TO_INLINE")
  inline fun messages(): Flow<Message> = messages

  @PublishedApi
  internal val messages = MutableSharedFlow<Message>()

  @Suppress("NOTHING_TO_INLINE")
  inline fun statusMessage(): StateFlow<StatusMessage?> = statusMessage

  @PublishedApi
  internal val statusMessage = MutableStateFlow<StatusMessage?>(null)
}
