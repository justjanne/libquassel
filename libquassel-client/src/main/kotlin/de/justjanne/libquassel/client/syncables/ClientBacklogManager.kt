/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.syncables

import de.justjanne.bitflags.none
import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.client.util.CoroutineKeyedQueue
import de.justjanne.libquassel.protocol.models.flags.MessageFlag
import de.justjanne.libquassel.protocol.models.flags.MessageFlags
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.common.BacklogManager
import de.justjanne.libquassel.protocol.variant.QVariantList

class ClientBacklogManager(
  session: Session
) : BacklogManager(session) {
  private val bufferQueue = CoroutineKeyedQueue<BacklogData.Buffer, QVariantList>()
  private val bufferFilteredQueue = CoroutineKeyedQueue<BacklogData.BufferFiltered, QVariantList>()
  private val bufferForwardQueue = CoroutineKeyedQueue<BacklogData.BufferForward, QVariantList>()
  private val allQueue = CoroutineKeyedQueue<BacklogData.All, QVariantList>()
  private val allFilteredQueue = CoroutineKeyedQueue<BacklogData.AllFiltered, QVariantList>()

  suspend fun backlog(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ): QVariantList =
    bufferQueue.wait(BacklogData.Buffer(bufferId, first, last, limit, additional)) {
      requestBacklog(bufferId, first, last, limit, additional)
    }

  suspend fun backlogFiltered(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: MessageTypes = MessageType.none(),
    flags: MessageFlags = MessageFlag.none()
  ): QVariantList =
    bufferFilteredQueue.wait(BacklogData.BufferFiltered(bufferId, first, last, limit, additional, type, flags)) {
      requestBacklogFiltered(bufferId, first, last, limit, additional, type.toBits().toInt(), flags.toBits().toInt())
    }

  suspend fun backlogForward(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    type: MessageTypes = MessageType.none(),
    flags: MessageFlags = MessageFlag.none()
  ): QVariantList =
    bufferForwardQueue.wait(BacklogData.BufferForward(bufferId, first, last, limit, type, flags)) {
      requestBacklogForward(bufferId, first, last, limit, type.toBits().toInt(), flags.toBits().toInt())
    }

  suspend fun backlogAll(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ): QVariantList =
    allQueue.wait(BacklogData.All(first, last, limit, additional)) {
      requestBacklogAll(first, last, limit, additional)
    }

  suspend fun backlogAllFiltered(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: MessageTypes = MessageType.none(),
    flags: MessageFlags = MessageFlag.none()
  ): QVariantList =
    allFilteredQueue.wait(BacklogData.AllFiltered(first, last, limit, additional, type, flags)) {
      requestBacklogAllFiltered(first, last, limit, additional, type.toBits().toInt(), flags.toBits().toInt())
    }

  override fun receiveBacklog(
    bufferId: BufferId,
    first: MsgId,
    last: MsgId,
    limit: Int,
    additional: Int,
    messages: QVariantList
  ) {
    bufferQueue.resume(
      BacklogData.Buffer(
        bufferId,
        first,
        last,
        limit,
        additional
      ),
      messages
    )
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
    bufferFilteredQueue.resume(
      BacklogData.BufferFiltered(
        bufferId,
        first,
        last,
        limit,
        additional,
        MessageType.of(type.toUInt()),
        MessageFlag.of(flags.toUInt())
      ),
      messages
    )
    super.receiveBacklogFiltered(bufferId, first, last, limit, additional, type, flags, messages)
  }

  override fun receiveBacklogForward(
    bufferId: BufferId,
    first: MsgId,
    last: MsgId,
    limit: Int,
    type: Int,
    flags: Int,
    messages: QVariantList
  ) {
    bufferForwardQueue.resume(
      BacklogData.BufferForward(
        bufferId,
        first,
        last,
        limit,
        MessageType.of(type.toUInt()),
        MessageFlag.of(flags.toUInt())
      ),
      messages
    )
    super.receiveBacklogForward(bufferId, first, last, limit, type, flags, messages)
  }

  override fun receiveBacklogAll(first: MsgId, last: MsgId, limit: Int, additional: Int, messages: QVariantList) {
    allQueue.resume(
      BacklogData.All(
        first,
        last,
        limit,
        additional
      ),
      messages
    )
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
    allFilteredQueue.resume(
      BacklogData.AllFiltered(
        first,
        last,
        limit,
        additional,
        MessageType.of(type.toUInt()),
        MessageFlag.of(flags.toUInt()),
      ),
      messages
    )
    super.receiveBacklogAllFiltered(first, last, limit, additional, type, flags, messages)
  }

  sealed class BacklogData {
    data class Buffer(
      val bufferId: BufferId,
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0
    ) : BacklogData()

    data class BufferFiltered(
      val bufferId: BufferId,
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0,
      val type: MessageTypes = MessageType.all,
      val flags: MessageFlags = MessageFlag.all
    ) : BacklogData()

    data class BufferForward(
      val bufferId: BufferId,
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val type: MessageTypes = MessageType.all,
      val flags: MessageFlags = MessageFlag.all
    ) : BacklogData()

    data class All(
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0
    ) : BacklogData()

    data class AllFiltered(
      val first: MsgId = MsgId(-1),
      val last: MsgId = MsgId(-1),
      val limit: Int = -1,
      val additional: Int = 0,
      val type: MessageTypes = MessageType.all,
      val flags: MessageFlags = MessageFlag.all
    ) : BacklogData()
  }
}
