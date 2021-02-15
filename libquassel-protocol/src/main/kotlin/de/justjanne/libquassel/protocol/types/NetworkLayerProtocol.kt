/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2020 Janne Mareike Koschinski
 * Copyright (c) 2020 The Quassel Project
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
    private val values = values().associateBy(NetworkLayerProtocol::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: UByte): NetworkLayerProtocol? = values[value]
  }
}
