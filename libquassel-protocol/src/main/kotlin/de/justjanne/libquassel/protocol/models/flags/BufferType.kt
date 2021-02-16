/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
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
