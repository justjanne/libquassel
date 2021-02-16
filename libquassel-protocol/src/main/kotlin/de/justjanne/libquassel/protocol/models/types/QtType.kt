/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.types

import de.justjanne.libquassel.protocol.serializers.NoSerializerForTypeException
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.serializers.qt.BoolSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ByteBufferSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ByteSerializer
import de.justjanne.libquassel.protocol.serializers.qt.DoubleSerializer
import de.justjanne.libquassel.protocol.serializers.qt.FloatSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.LongSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QCharSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QDateSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QDateTimeSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QStringListSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QTimeSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantListSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantMapSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ShortSerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf16
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UIntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ULongSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UShortSerializer
import de.justjanne.libquassel.protocol.serializers.qt.VoidSerializer

/**
 * Supported qt types for serialization
 */
enum class QtType(
  /**
   * Underlying representation
   */
  val id: kotlin.Int,
  /**
   * Serializer for data described by this type
   */
  @PublishedApi
  internal val serializer: PrimitiveSerializer<*>? = null
) {
  /**
   * Void, no data at all
   */
  Void(0, VoidSerializer),

  /**
   * 8-bit boolean, 0 is false, everything else is true
   * See [kotlin.Boolean]
   */
  Bool(1, BoolSerializer),

  /**
   * 8-bit signed integer
   * See [kotlin.Byte]
   */
  Char(131, ByteSerializer),

  /**
   * 8-bit unsigned integer
   * See [kotlin.UByte]
   */
  UChar(134, UByteSerializer),

  /**
   * 16-bit signed integer
   * See [kotlin.Short]
   */
  Short(130, ShortSerializer),

  /**
   * 16-bit unsigned integer
   * See [kotlin.UShort]
   */
  UShort(133, UShortSerializer),

  /**
   * 32-bit signed integer
   * See [kotlin.Int]
   */
  Int(2, IntSerializer),

  /**
   * 32-bit unsigned integer
   * See [kotlin.UInt]
   */
  UInt(3, UIntSerializer),

  /**
   * 64-bit signed integer
   * See [kotlin.Long]
   */
  Long(129, LongSerializer),

  /**
   * 64-bit unsigned integer
   * See [kotlin.ULong]
   */
  ULong(132, ULongSerializer),

  /**
   * 32-bit IEEE 754 float
   * See [kotlin.Float]
   */
  Float(135, FloatSerializer),

  /**
   * 64-bit IEEE 754 float
   * See [kotlin.Double]
   */
  Double(6, DoubleSerializer),

  /**
   * Date in the gregorian calender as julian date
   * See [org.threeten.bp.LocalDate]
   */
  QDate(14, QDateSerializer),

  /**
   * Relative time in milliseconds since midnight
   * See [org.threeten.bp.LocalTime]
   */
  QTime(15, QTimeSerializer),

  /**
   * Timestamp composed out of QDate, QTime and a zone specifier
   */
  QDateTime(16, QDateTimeSerializer),

  /**
   * 16-bit unicode character
   * See [kotlin.Char]
   */
  QChar(7, QCharSerializer),

  /**
   * UTF-16 big endian string
   * See [kotlin.String]
   */
  QString(10, StringSerializerUtf16),

  /**
   * Length prefixes list of UTF-16 big endian strings
   */
  QStringList(11, QStringListSerializer),

  /**
   * Length prefixed slice of binary data
   * See [java.nio.ByteBuffer]
   */
  QByteArray(12, ByteBufferSerializer),

  /**
   * Typed box
   * See [QVariant]
   */
  QVariant(138, QVariantSerializer),

  /**
   * Length prefixed map of [QString] to [QVariant]
   */
  QVariantMap(8, QVariantMapSerializer),

  /**
   * Length prefixed list of [QVariant]
   */
  QVariantList(9, QVariantListSerializer),

  /**
   * Custom data with a special (de-)serializer
   * See [QuasselType]
   */
  UserType(127);

  /**
   * Obtain a serializer for this type (type safe)
   */
  @Suppress("UNCHECKED_CAST")
  inline fun <reified T> serializer(): PrimitiveSerializer<T> {
    if (serializer?.javaType?.isAssignableFrom(T::class.java) == true) {
      return serializer as PrimitiveSerializer<T>
    } else {
      throw NoSerializerForTypeException.Qt(this, T::class.java)
    }
  }

  companion object {
    private val values = enumValues<QtType>()
      .associateBy(QtType::id)

    /**
     * Obtain a QtType by its underlying representation
     */
    fun of(id: kotlin.Int): QtType? = values[id]
  }
}
