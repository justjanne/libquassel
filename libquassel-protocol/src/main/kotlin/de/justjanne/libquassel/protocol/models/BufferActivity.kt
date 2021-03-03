/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

/**
 * Model representing all seen activity on a buffer
 */
enum class BufferActivity(
  /**
   * Underlying representation
   */
  val value: Int,
) {
  /**
   * Other, unspecified activity has occurred on this buffer (join, part, quit, etc)
   */
  OtherActivity(1),

  /**
   * A new unread mesage is available on this buffer
   */
  NewMessage(2),

  /**
   * A highlight for the current user is available on this buffer
   */
  Highlight(4);

  companion object {
    private val map = enumValues<BufferActivity>()
      .associateBy(BufferActivity::value)

    /**
     * Obtain a zone specification by its underlying representation
     */
    fun of(type: Int) = map[type]
  }
}
