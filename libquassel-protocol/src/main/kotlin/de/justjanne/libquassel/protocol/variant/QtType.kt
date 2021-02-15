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
 * with this program. If not, see <http:
 */

package de.justjanne.libquassel.protocol.variant

/**
 * Supported qt types for serialization
 */
enum class QtType(
  /**
   * Underlying representation
   */
  val id: kotlin.Int
) {
  /**
   * Void, no data at all
   */
  Void(0),

  /**
   * 8-bit boolean, 0 is false, everything else is true
   * See [kotlin.Boolean]
   */
  Bool(1),

  /**
   * 8-bit signed integer
   * See [kotlin.Byte]
   */
  Char(131),

  /**
   * 8-bit unsigned integer
   * See [kotlin.UByte]
   */
  UChar(134),

  /**
   * 16-bit signed integer
   * See [kotlin.Short]
   */
  Short(130),

  /**
   * 16-bit unsigned integer
   * See [kotlin.UShort]
   */
  UShort(133),

  /**
   * 32-bit signed integer
   * See [kotlin.Int]
   */
  Int(2),

  /**
   * 32-bit unsigned integer
   * See [kotlin.UInt]
   */
  UInt(3),

  /**
   * 64-bit signed integer
   * See [kotlin.Long]
   */
  Long(129),

  /**
   * 64-bit unsigned integer
   * See [kotlin.ULong]
   */
  ULong(132),

  /**
   * 32-bit IEEE 754 float
   * See [kotlin.Float]
   */
  Float(135),

  /**
   * 64-bit IEEE 754 float
   * See [kotlin.Double]
   */
  Double(6),

  /**
   * Date in the gregorian calender as julian date
   * See [org.threeten.bp.LocalDate]
   */
  QDate(14),

  /**
   * Relative time in milliseconds since midnight
   * See [org.threeten.bp.LocalTime]
   */
  QTime(15),

  /**
   * Timestamp composed out of QDate, QTime and a zone specifier
   */
  QDateTime(16),

  /**
   * 16-bit unicode character
   * See [kotlin.Char]
   */
  QChar(7),

  /**
   * UTF-16 big endian string
   * See [kotlin.String]
   */
  QString(10),

  /**
   * Length prefixes list of UTF-16 big endian strings
   */
  QStringList(11),

  /**
   * Length prefixed slice of binary data
   * See [java.nio.ByteBuffer]
   */
  QByteArray(12),

  /**
   * Typed box
   * See [QVariant]
   */
  QVariant(138),

  /**
   * Length prefixed map of [QString] to [QVariant]
   */
  QVariantMap(8),

  /**
   * Length prefixed list of [QVariant]
   */
  QVariantList(9),

  /**
   * Custom data with a special (de-)serializer
   * See [QuasselType]
   */
  UserType(127);

  companion object {
    private val values = values().associateBy(QtType::id)

    /**
     * Obtain a QtType by its underlying representation
     */
    fun of(id: kotlin.Int): QtType? = values[id]
  }
}
