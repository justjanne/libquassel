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

private typealias IdentityIdType = SignedIdType
/**
 * A [SignedId] for an identity object
 */
inline class IdentityId(
  /**
   * Native value
   */
  override val id: IdentityIdType
) : SignedId<IdentityIdType> {
  override fun toString() = "IdentityId($id)"

  companion object {
    /**
     * Lower limit for this type
     */
    val MIN_VALUE = IdentityId(IdentityIdType.MIN_VALUE)
    /**
     * Upper limit for this type
     */
    val MAX_VALUE = IdentityId(IdentityIdType.MAX_VALUE)
  }
}
