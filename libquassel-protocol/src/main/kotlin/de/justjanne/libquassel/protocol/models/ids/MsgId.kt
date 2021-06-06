/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.ids

private typealias MsgIdType = SignedId64Type

/**
 * A [SignedId] for an individual message
 * Warning: this is the only id which can be backed by a 64-bit value
 */
@JvmInline
value class MsgId(
  /**
   * Native value
   */
  override val id: MsgIdType
) : SignedId<MsgIdType> {
  override fun toString() = "MsgId($id)"

  companion object {
    /**
     * Lower limit for this type
     */
    val MIN_VALUE = MsgId(MsgIdType.MIN_VALUE)

    /**
     * Upper limit for this type
     */
    val MAX_VALUE = MsgId(MsgIdType.MAX_VALUE)
  }
}
