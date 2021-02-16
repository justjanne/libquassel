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
 * Model representing different types of messages
 */
enum class MessageType(
  override val value: UInt,
) : Flag<UInt> {
  /**
   * Plain text message
   */
  Plain(0x00001u),

  /**
   * Bot response/redirected notice
   */
  Notice(0x00002u),

  /**
   * Third-person message
   */
  Action(0x00004u),

  /**
   * Nick change
   */
  Nick(0x00008u),

  /**
   * Mode change
   */
  Mode(0x00010u),

  /**
   * Channel join
   */
  Join(0x00020u),

  /**
   * Channel leave
   */
  Part(0x00040u),

  /**
   * Network leave
   */
  Quit(0x00080u),

  /**
   * Involuntary channel leave
   */
  Kick(0x00100u),

  /**
   * Involuntary network leave
   */
  Kill(0x00200u),

  /**
   * Server message
   */
  Server(0x00400u),

  /**
   * Global info message
   */
  Info(0x00800u),

  /**
   * Server error
   */
  Error(0x01000u),

  /**
   * Midnight marker
   */
  DayChange(0x02000u),

  /**
   * Channel topic has changed
   */
  Topic(0x04000u),

  /**
   * Multiple users joined (usually due to a flaky server)
   */
  NetsplitJoin(0x08000u),

  /**
   * Multiple users left (usually due to a flaky server)
   */
  NetsplitQuit(0x10000u),

  /**
   * User invited to channel
   */
  Invite(0x20000u),

  /**
   * Last read marker
   */
  Markerline(0x40000u);

  companion object : Flags<UInt, MessageType> {
    private val values = enumValues<MessageType>()
      .associateBy(MessageType::value)
    override val all: MessageTypes = values.values.toEnumSet()
  }
}

/**
 * Model representing a bitfield of [MessageType] flags
 */
typealias MessageTypes = Set<MessageType>
