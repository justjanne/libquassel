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

enum class ChannelModeType(
  /**
   * Underlying representation
   */
  val value: UInt,
) {
  // NOT_A_CHANMODE(0x00u),
  A_CHANMODE(0x01u),
  B_CHANMODE(0x02u),
  C_CHANMODE(0x04u),
  D_CHANMODE(0x08u);

  companion object {
    private val values = enumValues<ChannelModeType>()
      .associateBy(ChannelModeType::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: UInt): ChannelModeType? = values[value]
  }
}
