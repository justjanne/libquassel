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
 * Mode for selecting the port range for DCC
 */
enum class DccPortSelectionMode(
  /**
   * Underlying representation
   */
  val value: UByte,
) {
  /**
   * Automatic port selection
   */
  Automatic(0x00u),

  /**
   * Manually specified port range
   */
  Manual(0x01u);

  companion object {
    private val values = enumValues<DccPortSelectionMode>()
      .associateBy(DccPortSelectionMode::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: UByte): DccPortSelectionMode? = values[value]
  }
}
