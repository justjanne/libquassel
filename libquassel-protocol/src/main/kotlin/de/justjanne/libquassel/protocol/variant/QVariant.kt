/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.variant

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.io.contentToString
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.util.instanceof
import de.justjanne.libquassel.protocol.util.subtype
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

  /**
   * Serializer for the contained data
   */
  abstract val serializer: PrimitiveSerializer<T>
  @PublishedApi
  internal abstract fun <U> withType(type: Class<U>): QVariant<U>?

  /**
   * QVariant of a predefined qt type
   */
  data class Typed<T> @PublishedApi internal constructor(
    override val data: T,
    @PublishedApi
    internal val type: QtType,
    override val serializer: PrimitiveSerializer<T>
  ) : QVariant<T>() {
    override fun <U> withType(type: Class<U>): QVariant<U>? {
      return if (
        type subtype this.type.serializer?.javaType &&
        data instanceof this.type.serializer?.javaType
      ) {
        @Suppress("UNCHECKED_CAST")
        this as QVariant<U>
      } else {
        null
      }
    }
    override fun toString() = data.let {
      when (it) {
        is ByteBuffer ->
          "QVariant(${type.name}, ${it.contentToString()})"
        else ->
          "QVariant(${type.name}, $it)"
      }
    }
  }

  /**
   * QVariant of an arbitrary custom type
   *
   * Serialized as [QtType.UserType] with the custom type name serialized as ASCII string
   */
  data class Custom<T> @PublishedApi internal constructor(
    override val data: T,
    @PublishedApi
    internal val type: QuasselType,
    override val serializer: PrimitiveSerializer<T>
  ) : QVariant<T>() {
    override fun <U> withType(type: Class<U>): QVariant<U>? {
      return if (
        type subtype this.type.serializer?.javaType &&
        data instanceof this.type.serializer?.javaType
      ) {
        @Suppress("UNCHECKED_CAST")
        this as QVariant<U>
      } else {
        null
      }
    }

    override fun toString() = data.let {
      when (it) {
        is ByteBuffer ->
          "QVariant(${type.name}, ${it.contentToString()})"
        else ->
          "QVariant(${type.name}, $it)"
      }
    }
  }

  @PublishedApi
  @Suppress("UNCHECKED_CAST")
  internal inline fun <reified U> withType(): QVariant<U>? =
    withType(U::class.java)

  fun type(): Class<*>? = data?.let { it::class.java }

  internal fun serialize(buffer: ChainedByteBuffer, featureSet: FeatureSet) =
    serializer.serialize(buffer, data, featureSet)
}

/**
 * Construct a QVariant from data and type
 */
inline fun <reified T> qVariant(data: T, type: QtType): QVariant<T> =
  QVariant.Typed(data, type, type.serializer())

/**
 * Construct a QVariant from data and type
 */
inline fun <reified T> qVariant(data: T, type: QuasselType): QVariant<T> =
  QVariant.Custom(data, type, type.serializer())

/**
 * Extract the content of a QVariant in a type-safe manner
 * @return value of the QVariant or null
 */
inline fun <reified T> QVariant_?.into(): T? =
  this?.withType<T>()?.data

/**
 * Extract the content of a QVariant in a type-safe manner
 * @return value of the QVariant
 * @throws if the value could not be coerced into the given type
 */
inline fun <reified T> QVariant_?.intoOrThrow(): T =
  this?.withType<T>()?.data ?: throw WrongVariantTypeException(
    T::class.java.canonicalName,
    this?.type()?.canonicalName ?: "null"
  )

/**
 * Extract the content of a QVariant in a type-safe manner
 * @param defValue default value if the type does not match or no value is found
 * @return value of the QVariant or defValue
 */
inline fun <reified T> QVariant_?.into(defValue: T): T =
  this?.withType<T>()?.data ?: defValue
