/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.dcc

/**
 * Mode for detecting the outgoing IP
 */
enum class DccIpDetectionMode(
  /**
   * Underlying representation
   */
  val value: UByte,
) {
  /**
   * Automatic detection
   */
  Automatic(0x00u),

  /**
   * Manually specified
   */
  Manual(0x01u);

  companion object {
    private val values = enumValues<DccIpDetectionMode>()
      .associateBy(DccIpDetectionMode::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: UByte): DccIpDetectionMode? = values[value]
  }
}
