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

import de.justjanne.bitflags.none
import de.justjanne.libquassel.protocol.models.flags.BufferActivities
import de.justjanne.libquassel.protocol.models.flags.BufferActivity
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.flags.BufferTypes
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.NetworkId

data class BufferViewConfigState(
  val bufferViewId: Int,
  val bufferViewName: String = "",
  val networkId: NetworkId = NetworkId(0),
  val addNewBuffersAutomatically: Boolean = true,
  val sortAlphabetically: Boolean = true,
  val hideInactiveBuffers: Boolean = false,
  val hideInactiveNetworks: Boolean = false,
  val disableDecoration: Boolean = false,
  val allowedBufferTypes: BufferTypes = BufferType.all,
  val minimumActivity: BufferActivities = BufferActivity.none(),
  val showSearch: Boolean = false,
  val buffers: List<BufferId> = emptyList(),
  val removedBuffers: Set<BufferId> = emptySet(),
  val temporarilyRemovedBuffers: Set<BufferId> = emptySet(),
)