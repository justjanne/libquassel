/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
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
