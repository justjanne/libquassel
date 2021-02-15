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

enum class QtType(val id: kotlin.Int) {
  Void(0),
  Bool(1),
  Char(131),
  UChar(134),
  Short(130),
  UShort(133),
  Int(2),
  UInt(3),
  Long(129),
  ULong(132),
  Float(135),
  Double(6),
  QDate(14),
  QTime(15),
  QDateTime(16),
  QChar(7),
  QString(10),
  QStringList(11),
  QByteArray(12),
  QVariant(138),
  QVariantMap(8),
  QVariantList(9),
  UserType(127);

  companion object {
    private val values = values().associateBy(QtType::id)
    fun of(id: kotlin.Int): QtType? = values[id]
  }
}
