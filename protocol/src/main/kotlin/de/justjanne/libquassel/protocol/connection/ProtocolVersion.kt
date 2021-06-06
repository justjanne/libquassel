/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
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
