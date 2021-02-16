/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

/**
 * Network protocol version
 */
enum class NetworkLayerProtocol(
  /**
   * Underlying representation
   */
  val value: UByte,
) {
  /**
   * Internet Protocol Version 4
   *
   * See [RFC-791](https://tools.ietf.org/html/rfc791)
   */
  IPv4Protocol(0x00u),
  /**
   * Internet Protocol Version 6
   *
   * See [RFC-2460](https://tools.ietf.org/html/rfc2460)
   * See [RFC-8200](https://tools.ietf.org/html/rfc8200)
   */
  IPv6Protocol(0x01u),

  /**
   * Any other IP based protocol
   */
  AnyIPProtocol(0x02u),

  /**
   * Any other network protocol
   */
  UnknownNetworkLayerProtocol(0xFFu);

  companion object {
    private val values = enumValues<NetworkLayerProtocol>()
      .associateBy(NetworkLayerProtocol::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: UByte): NetworkLayerProtocol? = values[value]
  }
}
