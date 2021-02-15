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
