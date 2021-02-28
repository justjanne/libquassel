/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.ids

import java.io.Serializable

/**
 * Native representation of a SignedId
 */
typealias SignedIdType = Int
/**
 * Native representation of a SignedId64
 */
typealias SignedId64Type = Long

/**
 * Base type for a signed id
 */
interface SignedId<T> : Serializable, Comparable<SignedId<T>>
  where T : Number, T : Comparable<T> {
  /**
   * Underlying representation
   */
  val id: T

  override fun compareTo(other: SignedId<T>): Int {
    return id.compareTo(other.id)
  }
}

/**
 * Validity check for a signed id
 */
@Suppress("NOTHING_TO_INLINE")
@JvmName("isValidId")
inline fun SignedId<SignedIdType>.isValid() = id > 0

/**
 * Validity check for a signed id
 */
@Suppress("NOTHING_TO_INLINE")
@JvmName("isValidId64")
inline fun SignedId<SignedId64Type>.isValid() = id > 0
