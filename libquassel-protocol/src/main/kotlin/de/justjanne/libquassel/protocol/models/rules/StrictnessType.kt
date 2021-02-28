/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.rules

enum class StrictnessType(
  val value: Int
) {
  UnmatchedStrictness(0),
  SoftStrictness(1),
  HardStrictness(2);

  companion object {
    private val values = enumValues<StrictnessType>()
      .associateBy(StrictnessType::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: Int): StrictnessType? = values[value]
  }
}
