/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

import de.justjanne.libquassel.protocol.models.flags.MessageFlags
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.models.ids.MsgId
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
   * `nick!ident@host` of the sender
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
