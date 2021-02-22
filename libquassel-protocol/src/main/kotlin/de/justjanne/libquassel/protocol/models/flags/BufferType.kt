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

package de.justjanne.libquassel.protocol.models.flags

import de.justjanne.bitflags.Flag
import de.justjanne.bitflags.Flags
import de.justjanne.bitflags.toEnumSet

/**
 * Model representing different types of chats
 */
enum class BufferType(
  override val value: UShort,
) : Flag<UShort> {
  /**
   * Status chat with technical data and internal information
   */
  Status(0x01u),

  /**
   * Named channel with potentially multiple users
   */
  Channel(0x02u),

  /**
   * Direct chat with a single other user
   */
  Query(0x04u),

  /**
   * Unnamed group between multiple users
   */
  Group(0x08u);

  companion object : Flags<UShort, BufferType> {
    private val values = enumValues<BufferType>()
      .associateBy(BufferType::value)
    override val all: BufferTypes = values.values.toEnumSet()
  }
}

/**
 * Model representing a bitfield of [BufferType] flags
 */
typealias BufferTypes = Set<BufferType>
