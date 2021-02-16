/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
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
