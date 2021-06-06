/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.ids

private typealias NetworkIdType = SignedIdType

/**
 * A [SignedId] for an irc network
 */
inline class NetworkId(
  /**
   * Native value
   */
  override val id: NetworkIdType
) : SignedId<NetworkIdType> {
  override fun toString() = "NetworkId($id)"

  companion object {
    /**
     * Lower limit for this type
     */
    val MIN_VALUE = NetworkId(NetworkIdType.MIN_VALUE)

    /**
     * Upper limit for this type
     */
    val MAX_VALUE = NetworkId(NetworkIdType.MAX_VALUE)
  }
}
