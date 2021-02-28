/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.syncables

import de.justjanne.bitflags.of
import de.justjanne.libquassel.protocol.models.flags.MessageFlag
import de.justjanne.libquassel.protocol.models.flags.MessageFlags
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.syncables.BacklogManager
import de.justjanne.libquassel.protocol.syncables.Session
import de.justjanne.libquassel.protocol.variant.QVariantList
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ClientBacklogManager(
  session: Session
) : BacklogManager(session) {
  private val bufferListeners =
    mutableMapOf<BacklogData.Buffer, Continuation<BacklogData.Buffer>>()
  private val bufferFilteredListeners =
    mutableMapOf<BacklogData.BufferFiltered, Continuation<BacklogData.BufferFiltered>>()
  private val allListeners =
    mutableMapOf<BacklogData.All, Continuation<BacklogData.All>>()
  private val allFilteredListeners =
    mutableMapOf<BacklogData.AllFiltered, Continuation<BacklogData.AllFiltered>>()

  suspend fun backlog(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ) = suspendCoroutine<BacklogData.Buffer> {
    val data = BacklogData.Buffer(bufferId, first, last, limit, additional)
    bufferListeners[data] = it
  }

  suspend fun backlogFiltered(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: MessageTypes = MessageType.all,
    flags: MessageFlags = MessageFlag.all
  ) = suspendCoroutine<BacklogData.BufferFiltered> {
    val data = BacklogData.BufferFiltered(bufferId, first, last, limit, additional, type, flags)
    bufferFilteredListeners[data] = it
  }

  suspend fun backlogAll(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ) = suspendCoroutine<BacklogData.All> {
    val data = BacklogData.All(first, last, limit, additional)
    allListeners[data] = it
  }

  suspend fun backlogAllFiltered(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: MessageTypes = MessageType.all,
    flags: MessageFlags = MessageFlag.all
  ) = suspendCoroutine<BacklogData.AllFiltered> {
    val data = BacklogData.AllFiltered(first, last, limit, additional, type, flags)
    allFilteredListeners[data] = it
  }

  override fun receiveBacklog(
    bufferId: BufferId,
    first: MsgId,
    last: MsgId,
    limit: Int,
    additional: Int,
    messages: QVariantList
  ) {
    val data = BacklogData.Buffer(
      bufferId,
      first,
      last,
      limit,
      additional
    )
    bufferListeners[data]?.resume(data.copy(messages = messages))
    super.receiveBacklog(bufferId, first, last, limit, additional, messages)
  }

  override fun receiveBacklogFiltered(
    bufferId: BufferId,
    first: MsgId,
    last: MsgId,
    limit: Int,
    additional: Int,
    type: Int,
    flags: Int,
    messages: QVariantList
  ) {
    val data = BacklogData.BufferFiltered(
      bufferId,
      first,
      last,
      limit,
      additional,
      MessageType.of(type.toUInt()),
      MessageFlag.of(flags.toUInt())
    )
    bufferFilteredListeners[data]?.resume(data.copy(messages = messages))
    super.receiveBacklogFiltered(bufferId, first, last, limit, additional, type, flags, messages)
  }

  override fun receiveBacklogAll(first: MsgId, last: MsgId, limit: Int, additional: Int, messages: QVariantList) {
    val data = BacklogData.All(
      first,
      last,
      limit,
      additional
    )
    allListeners[data]?.resume(data.copy(messages = messages))
    super.receiveBacklogAll(first, last, limit, additional, messages)
  }

  override fun receiveBacklogAllFiltered(
    first: MsgId,
    last: MsgId,
    limit: Int,
    additional: Int,
    type: Int,
    flags: Int,
    messages: QVariantList
  ) {
    val data = BacklogData.AllFiltered(
      first,
      last,
      limit,
      additional,
      MessageType.of(type.toUInt()),
      MessageFlag.of(flags.toUInt()),
    )
    allFilteredListeners[data]?.resume(data.copy(messages = messages))
    super.receiveBacklogAllFiltered(first, last, limit, additional, type, flags, messages)
  }

  sealed class BacklogData {
    data class Buffer(
      val bufferId: BufferId,
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0,
      val messages: QVariantList = emptyList()
    ) : BacklogData()

    data class BufferFiltered(
      val bufferId: BufferId,
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0,
      val type: MessageTypes = MessageType.all,
      val flags: MessageFlags = MessageFlag.all,
      val messages: QVariantList = emptyList()
    ) : BacklogData()

    data class All(
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0,
      val messages: QVariantList = emptyList()
    ) : BacklogData()

    data class AllFiltered(
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0,
      val type: MessageTypes = MessageType.all,
      val flags: MessageFlags = MessageFlag.all,
      val messages: QVariantList = emptyList()
    ) : BacklogData()
  }
}
