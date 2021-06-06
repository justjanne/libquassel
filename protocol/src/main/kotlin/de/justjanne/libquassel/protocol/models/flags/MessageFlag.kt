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
