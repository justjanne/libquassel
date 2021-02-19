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

enum class HighlightNickType(
  val value: UByte,
) {
  NoNick(0x00u),
  CurrentNick(0x01u),
  AllNicks(0x02u);

  companion object {
    private val values = enumValues<HighlightNickType>()
      .associateBy(HighlightNickType::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: UByte): HighlightNickType? = values[value]
  }
}
