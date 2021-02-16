/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

import de.justjanne.bitflags.none
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.flags.BufferTypes
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.NetworkId

/**
 * Model object representing metadata for a single chat
 */
data class BufferInfo(
  /**
   * Id of the chat
   */
  val bufferId: BufferId = BufferId(-1),
  /**
   * Id of the network to which this chat belongs
   */
  val networkId: NetworkId = NetworkId(-1),
  /**
   * type of this chat
   */
  val type: BufferTypes = BufferType.none(),
  /**
   * Id of the group to which this chat belongs
   */
  val groupId: Int = -1,
  /**
   * User-visible name of this chat
   */
  val bufferName: String? = null,
)
