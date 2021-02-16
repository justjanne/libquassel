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
    private val map = values().associateBy(TimeSpec::value)

    /**
     * Obtain a zone specification by its underlying representation
     */
    fun of(type: Byte) = map[type]
  }
}
