/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.flags.BufferTypes
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.ids.NetworkId

data class BufferSyncerState(
  val activities: Map<BufferId, MessageTypes> = emptyMap(),
  val highlightCounts: Map<BufferId, Int> = emptyMap(),
  val lastSeenMsg: Map<BufferId, MsgId> = emptyMap(),
  val markerLines: Map<BufferId, MsgId> = emptyMap(),
  val bufferInfos: Map<BufferId, BufferInfo> = emptyMap()
) {
  fun where(
    bufferName: String? = null,
    bufferId: BufferId? = null,
    networkId: NetworkId? = null,
    type: BufferTypes? = null,
    groupId: Int? = null,
    networkState: NetworkState? = null
  ) = bufferInfos.values.asSequence()
    .filter {
      bufferName == null ||
        networkState == null ||
        networkState.caseMapper().equalsIgnoreCase(it.bufferName, bufferName)
    }
    .filter { bufferId == null || it.bufferId == bufferId }
    .filter { networkId == null || it.networkId == networkId }
    .filter { type == null || it.type == type }
    .filter { groupId == null || it.groupId == groupId }

  fun find(
    bufferName: String? = null,
    bufferId: BufferId? = null,
    networkId: NetworkId? = null,
    type: BufferTypes? = null,
    groupId: Int? = null,
    networkState: NetworkState? = null
  ) = where(bufferName, bufferId, networkId, type, groupId, networkState).firstOrNull()

  fun all(
    bufferName: String? = null,
    bufferId: BufferId? = null,
    networkId: NetworkId? = null,
    type: BufferTypes? = null,
    groupId: Int? = null,
    networkState: NetworkState? = null
  ) = where(bufferName, bufferId, networkId, type, groupId, networkState).toList()
}
