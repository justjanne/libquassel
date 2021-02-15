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

package de.justjanne.libquassel.protocol.types

import org.threeten.bp.Instant

/**
 * Model representing a chat message
 */
data class Message(
  /**
   * Id of the message
   */
  val messageId: MsgId,
  /**
   * Timestamp at which the message was sent
   */
  val time: Instant,
  /**
   * Message type
   */
  val type: MessageTypes,
  /**
   * Set flags on the message
   */
  val flag: MessageFlags,
  /**
   * Metadata of the buffer the message was received in
   */
  val bufferInfo: BufferInfo,
  /**
   * nick!ident@host of the sender
   */
  val sender: String,
  /**
   * Channel role prefixes of the sender
   */
  val senderPrefixes: String,
  /**
   * Realname of the sender
   */
  val realName: String,
  /**
   * Avatar of the sender
   */
  val avatarUrl: String,
  /**
   * Message content
   */
  val content: String
) {
  override fun toString(): String {
    return "Message(" +
      "messageId=$messageId, " +
      "time=$time, " +
      "type=$type, " +
      "flag=$flag, " +
      "bufferInfo=$bufferInfo, " +
      "sender='$sender', " +
      "senderPrefixes='$senderPrefixes', " +
      "content='$content'" +
      ")"
  }
}
