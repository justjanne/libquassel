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

package de.justjanne.libquassel.protocol.variant

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.io.contentToString
import de.justjanne.libquassel.protocol.serializers.QtSerializer
import de.justjanne.libquassel.protocol.serializers.QtSerializers
import de.justjanne.libquassel.protocol.serializers.QuasselSerializer
import de.justjanne.libquassel.protocol.serializers.QuasselSerializers
import java.nio.ByteBuffer

/**
 * Simple alias for a generic QVariant type
 */
typealias QVariant_ = QVariant<*>

/**
 * Variant box for generic types
 */
sealed class QVariant<T> {
  @PublishedApi
  internal abstract val data: T

  @PublishedApi
  internal abstract val serializer: QtSerializer<T>

  /**
   * QVariant of a predefined qt type
   */
  data class Typed<T> @PublishedApi internal constructor(
    override val data: T,
    override val serializer: QtSerializer<T>
  ) : QVariant<T>() {
    override fun toString() = super.toString()
  }

  /**
   * QVariant of an arbitrary custom type
   *
   * Serialized as [QtType.UserType] with the custom type name serialized as ASCII string
   */
  data class Custom<T> @PublishedApi internal constructor(
    override val data: T,
    override val serializer: QuasselSerializer<T>
  ) : QVariant<T>() {
    override fun toString() = super.toString()
  }

  internal fun serialize(buffer: ChainedByteBuffer, featureSet: FeatureSet) {
    serializer.serialize(buffer, data, featureSet)
  }

  override fun toString() = data.let {
    when (it) {
      is ByteBuffer ->
        "QVariant(${serializer::class.java.simpleName}, ${it.contentToString()})"
      else ->
        "QVariant(${serializer::class.java.simpleName}, $it)"
    }
  }

  @PublishedApi
  @Suppress("UNCHECKED_CAST")
  internal inline fun <reified T> withType(): QVariant<T>? =
    if (serializer.javaType == T::class.java && this.data is T) this as QVariant<T>
    else null
}

/**
 * Construct a QVariant from data and type
 */
inline fun <reified T> qVariant(data: T, type: QtType): QVariant<T> =
  QVariant.Typed(data, QtSerializers.find(type))

/**
 * Construct a QVariant from data and type
 */
inline fun <reified T> qVariant(data: T, type: QuasselType): QVariant<T> =
  QVariant.Custom(data, QuasselSerializers.find(type))

/**
 * Extract the content of a QVariant in a type-safe manner
 * @return value of the QVariant or null
 */
inline fun <reified T> QVariant_?.into(): T? =
  this?.withType<T>()?.data

/**
 * Extract the content of a QVariant in a type-safe manner
 * @param defValue default value if the type does not match or no value is found
 * @return value of the QVariant or defValue
 */
inline fun <reified T> QVariant_?.into(defValue: T): T =
  this?.withType<T>()?.data ?: defValue
