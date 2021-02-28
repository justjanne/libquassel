/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
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
 * Model representing all seen activity on a buffer
 */
enum class BufferActivity(
  override val value: UInt,
) : Flag<UInt> {
  /**
   * Other, unspecified activity has occurred on this buffer (join, part, quit, etc)
   */
  OtherActivity(0x01u),

  /**
   * A new unread mesage is available on this buffer
   */
  NewMessage(0x02u),

  /**
   * A highlight for the current user is available on this buffer
   */
  Highlight(0x04u);

  companion object : Flags<UInt, BufferActivity> {
    private val values = enumValues<BufferActivity>()
      .associateBy(BufferActivity::value)
    override val all: BufferActivities = values.values.toEnumSet()
  }
}

/**
 * Model representing a bitfield of [BufferActivity] flags
 */
typealias BufferActivities = Set<BufferActivity>
