/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.ids

private typealias BufferIdType = SignedIdType

/**
 * A [SignedId] for a chat/buuffer
 */
inline class BufferId(
  /**
   * Native value
   */
  override val id: BufferIdType
) : SignedId<BufferIdType> {
  override fun toString() = "BufferId($id)"

  companion object {
    /**
     * Lower limit for this type
     */
    val MIN_VALUE = BufferId(BufferIdType.MIN_VALUE)

    /**
     * Upper limit for this type
     */
    val MAX_VALUE = BufferId(BufferIdType.MAX_VALUE)
  }
}
