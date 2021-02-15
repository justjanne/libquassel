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

package de.justjanne.libquassel.protocol.serializers

import de.justjanne.libquassel.protocol.serializers.qt.BoolSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ByteBufferSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ByteSerializer
import de.justjanne.libquassel.protocol.serializers.qt.DateSerializer
import de.justjanne.libquassel.protocol.serializers.qt.DateTimeSerializer
import de.justjanne.libquassel.protocol.serializers.qt.DoubleSerializer
import de.justjanne.libquassel.protocol.serializers.qt.FloatSerializer
import de.justjanne.libquassel.protocol.serializers.qt.IntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.LongSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QCharSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QStringListSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantListSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantMapSerializer
import de.justjanne.libquassel.protocol.serializers.qt.QVariantSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ShortSerializer
import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf16
import de.justjanne.libquassel.protocol.serializers.qt.TimeSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UByteSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UIntSerializer
import de.justjanne.libquassel.protocol.serializers.qt.ULongSerializer
import de.justjanne.libquassel.protocol.serializers.qt.UShortSerializer
import de.justjanne.libquassel.protocol.serializers.qt.VoidSerializer
import de.justjanne.libquassel.protocol.variant.QtType

/**
 * Singleton object containing all serializers for qt types
 */
object QtSerializers {
  private val serializers = setOf<QtSerializer<*>>(
    VoidSerializer,
    BoolSerializer,

    ByteSerializer,
    UByteSerializer,
    ShortSerializer,
    UShortSerializer,
    IntSerializer,
    UIntSerializer,
    LongSerializer,
    ULongSerializer,

    FloatSerializer,
    DoubleSerializer,

    QCharSerializer,
    StringSerializerUtf16,
    QStringListSerializer,
    ByteBufferSerializer,

    DateSerializer,
    TimeSerializer,
    DateTimeSerializer,

    QVariantSerializer,
    QVariantListSerializer,
    QVariantMapSerializer,
  ).associateBy(QtSerializer<*>::qtType)

  /**
   * Obtain the serializer for a qt type
   */
  operator fun get(type: QtType) = serializers[type]

  /**
   * Obtain the serializer for a qt type (type safe)
   */
  @Suppress("UNCHECKED_CAST")
  inline fun <reified T> find(type: QtType): QtSerializer<T> {
    val serializer = get(type)
      ?: throw NoSerializerForTypeException.Qt(type, T::class.java)
    if (serializer.javaType == T::class.java) {
      return serializer as QtSerializer<T>
    } else {
      throw NoSerializerForTypeException.Qt(type, T::class.java)
    }
  }
}
