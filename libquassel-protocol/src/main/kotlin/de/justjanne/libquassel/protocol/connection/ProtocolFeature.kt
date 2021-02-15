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

import de.justjanne.bitflags.Flag
import de.justjanne.bitflags.Flags
import de.justjanne.bitflags.toEnumSet

/**
 * Model representing protocol-level features
 */
enum class ProtocolFeature(
  override val value: UByte,
) : Flag<UByte> {
  /**
   * Model representing whether TLS is supported
   */
  TLS(0x01u),
  /**
   * Model representing whether DEFLATE compression is supported
   */
  Compression(0x02u);

  companion object : Flags<UByte, ProtocolFeature> {
    private val values = values().associateBy(ProtocolFeature::value)
    override val all: ProtocolFeatures = values.values.toEnumSet()
  }
}

/**
 * Model representing a bitfield of [ProtocolFeature] flags
 */
typealias ProtocolFeatures = Set<ProtocolFeature>
