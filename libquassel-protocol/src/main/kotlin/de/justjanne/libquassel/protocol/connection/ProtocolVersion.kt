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

package de.justjanne.libquassel.protocol.connection

/**
 * Model representing the possible protocol versions
 */
enum class ProtocolVersion(
  /**
   * Protocol version id
   */
  val value: UByte,
) {
  /**
   * Legacy protocol, used up til Quassel 0.10, not fully supported by this library
   */
  Legacy(0x01u),

  /**
   * Datastream protocol, introduced in Quassel 0.10.
   */
  Datastream(0x02u);

  companion object {
    private val values = values().associateBy(ProtocolVersion::value)

    /**
     * Obtain the protocol version for a protocol version id
     */
    fun of(value: UByte): ProtocolVersion = values[value]
      ?: throw IllegalArgumentException("Protocol not supported: $value")
  }
}
