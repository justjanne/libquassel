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
 * Model representing different types of message flags
 */
enum class MessageFlag(
  override val value: UInt,
) : Flag<UInt> {
  /**
   * Self-Message
   *
   * Sent by same user
   */
  Self(0x01u),

  /**
   * Highlight
   *
   * User was mentioned
   */
  Highlight(0x02u),

  /**
   * Redirected
   *
   * Message was redirected
   */
  Redirected(0x04u),

  /**
   * ServerMsg
   *
   * Server info message
   */
  ServerMsg(0x08u),

  /**
   * Backlog
   *
   * Message was loaded from history
   */
  Backlog(0x80u);

  companion object : Flags<UInt, MessageFlag> {
    private val values = enumValues<MessageFlag>()
      .associateBy(MessageFlag::value)
    override val all: MessageFlags = values.values.toEnumSet()
  }
}

/**
 * Model representing a bitfield of [MessageFlag] flags
 */
typealias MessageFlags = Set<MessageFlag>
