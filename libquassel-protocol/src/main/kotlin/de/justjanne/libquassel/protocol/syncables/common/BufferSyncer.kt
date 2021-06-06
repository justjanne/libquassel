/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.common

import de.justjanne.bitflags.none
import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.ids.isValid
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableObject
import de.justjanne.libquassel.protocol.syncables.state.BufferSyncerState
import de.justjanne.libquassel.protocol.syncables.stubs.BufferSyncerStub
import de.justjanne.libquassel.protocol.util.collections.pairs
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

open class BufferSyncer(
  session: Session? = null,
  state: BufferSyncerState = BufferSyncerState()
) : StatefulSyncableObject<BufferSyncerState>(session, "BufferSyncer", state),
  BufferSyncerStub {
  override fun toVariantMap() = mapOf(
    "Activities" to qVariant(
      state().activities.flatMap { (key, value) ->
        listOf(
          qVariant(key, QuasselType.BufferId),
          qVariant(value.toBits(), QtType.UInt)
        )
      },
      QtType.QVariantList
    ),
    "HighlightCounts" to qVariant(
      state().highlightCounts.flatMap { (key, value) ->
        listOf(
          qVariant(key, QuasselType.BufferId),
          qVariant(value, QtType.Int)
        )
      },
      QtType.QVariantList
    ),
    "LastSeenMsg" to qVariant(
      state().lastSeenMsg.flatMap { (key, value) ->
        listOf(
          qVariant(key, QuasselType.BufferId),
          qVariant(value, QuasselType.MsgId)
        )
      },
      QtType.QVariantList
    ),
    "MarkerLines" to qVariant(
      state().markerLines.flatMap { (key, value) ->
        listOf(
          qVariant(key, QuasselType.BufferId),
          qVariant(value, QuasselType.MsgId)
        )
      },
      QtType.QVariantList
    ),
  )

  override fun fromVariantMap(properties: QVariantMap) {
    state.update {
      copy(
        activities = properties["Activities"].into<QVariantList>()?.pairs { a, b ->
          Pair(
            a.into<BufferId>() ?: return@pairs null,
            MessageType.of(b.into<UInt>() ?: return@pairs null)
          )
        }?.filterNotNull()?.toMap().orEmpty(),
        highlightCounts = properties["HighlightCounts"].into<QVariantList>()?.pairs { a, b ->
          Pair(
            a.into<BufferId>() ?: return@pairs null,
            b.into<Int>() ?: return@pairs null
          )
        }?.filterNotNull()?.toMap().orEmpty(),
        lastSeenMsg = properties["LastSeenMsg"].into<QVariantList>()?.pairs { a, b ->
          Pair(
            a.into<BufferId>() ?: return@pairs null,
            b.into<MsgId>() ?: return@pairs null
          )
        }?.filterNotNull()?.toMap().orEmpty(),
        markerLines = properties["MarkerLines"].into<QVariantList>()?.pairs { a, b ->
          Pair(
            a.into<BufferId>() ?: return@pairs null,
            b.into<MsgId>() ?: return@pairs null
          )
        }?.filterNotNull()?.toMap().orEmpty()
      )
    }
    initialized = true
  }

  fun lastSeenMsg(buffer: BufferId): MsgId = state().lastSeenMsg[buffer] ?: MsgId(0)

  fun markerLine(buffer: BufferId): MsgId = state().markerLines[buffer] ?: MsgId(0)

  fun activity(buffer: BufferId): MessageTypes =
    state().activities[buffer] ?: MessageType.none()

  fun highlightCount(buffer: BufferId): Int = state().highlightCounts[buffer] ?: 0

  fun bufferInfo(bufferId: BufferId) = state().bufferInfos[bufferId]

  fun bufferInfos(): Collection<BufferInfo> = state().bufferInfos.values.toList()

  override fun mergeBuffersPermanently(buffer: BufferId, buffer2: BufferId) {
    removeBuffer(buffer2)
    super.mergeBuffersPermanently(buffer, buffer2)
  }

  override fun removeBuffer(buffer: BufferId) {
    state.update {
      copy(
        activities = activities - buffer,
        lastSeenMsg = lastSeenMsg - buffer,
        markerLines = markerLines - buffer,
        highlightCounts = highlightCounts - buffer,
        bufferInfos = bufferInfos - buffer
      )
    }
    super.removeBuffer(buffer)
  }

  override fun setLastSeenMsg(buffer: BufferId, msgId: MsgId) {
    if (!msgId.isValid() || lastSeenMsg(buffer) >= msgId) {
      return
    }

    state.update {
      copy(lastSeenMsg = lastSeenMsg + Pair(buffer, msgId))
    }

    super.setLastSeenMsg(buffer, msgId)
  }

  override fun setMarkerLine(buffer: BufferId, msgId: MsgId) {
    if (!msgId.isValid() || markerLine(buffer) >= msgId) {
      return
    }

    state.update {
      copy(markerLines = markerLines + Pair(buffer, msgId))
    }

    super.setMarkerLine(buffer, msgId)
  }

  override fun setBufferActivity(buffer: BufferId, types: Int) {
    state.update {
      copy(activities = activities + Pair(buffer, MessageType.of(types.toUInt())))
    }

    super.setBufferActivity(buffer, types)
  }

  fun setBufferActivity(buffer: BufferId, types: MessageTypes) {
    val oldTypes = activity(buffer)

    state.update {
      copy(activities = activities + Pair(buffer, types))
    }

    if ((types - oldTypes).isNotEmpty()) {
      val bufferInfo = bufferInfo(buffer)

      if (bufferInfo != null) {
        session?.bufferViewManager?.handleBuffer(bufferInfo, true)
      }
    }

    super.setBufferActivity(buffer, types.toBits().toInt())
  }

  override fun setHighlightCount(buffer: BufferId, count: Int) {
    state.update {
      copy(highlightCounts = highlightCounts + Pair(buffer, count))
    }
    super.setHighlightCount(buffer, count)
  }

  fun bufferInfoUpdated(info: BufferInfo) {
    val oldInfo = bufferInfo(info.bufferId)
    if (info != oldInfo) {
      state.update {
        copy(bufferInfos = bufferInfos + Pair(info.bufferId, info))
      }

      if (oldInfo != null) {
        session?.bufferViewManager?.handleBuffer(info)
      }
    }
  }
}
