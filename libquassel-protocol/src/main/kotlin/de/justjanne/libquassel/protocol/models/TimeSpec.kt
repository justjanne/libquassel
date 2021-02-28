/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

/**
 * Zoned definition for timestamps
 */
enum class TimeSpec(
  /**
   * Underlying representation
   */
  val value: Byte
) {
  /**
   * Unknown zone data
   * Should be treated like [LocalStandard]
   */
  LocalUnknown(-1),

  /**
   * Local zone data
   * Should be serialized as local time without DST
   */
  LocalStandard(0),

  /**
   * Local zone data
   * Should be serialized as local time with DST, if applicable
   */
  LocalDST(1),

  /**
   * Universal Time Coordinated
   * Should be treated as a zone offset of 0
   */
  UTC(2),

  /**
   * Time with specified offset in seconds
   */
  OffsetFromUTC(3);

  companion object {
    private val map = enumValues<TimeSpec>()
      .associateBy(TimeSpec::value)

    /**
     * Obtain a zone specification by its underlying representation
     */
    fun of(type: Byte) = map[type]
  }
}
