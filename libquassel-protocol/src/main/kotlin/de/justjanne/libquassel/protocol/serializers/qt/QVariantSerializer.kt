/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.serializers.NoSerializerForTypeException
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import de.justjanne.libquassel.protocol.variant.QVariant
import de.justjanne.libquassel.protocol.variant.QVariant_
import java.nio.ByteBuffer

/**
 * Serializer for [QVariant]
 */
object QVariantSerializer : PrimitiveSerializer<QVariant_> {
  override val javaType: Class<QVariant_> = QVariant::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: QVariant_, featureSet: FeatureSet) {
    when (data) {
      is QVariant.Typed -> {
        IntSerializer.serialize(buffer, data.type.id, featureSet)
        BoolSerializer.serialize(buffer, false, featureSet)
        data.serialize(buffer, featureSet)
      }
      is QVariant.Custom -> {
        IntSerializer.serialize(buffer, QtType.UserType.id, featureSet)
        BoolSerializer.serialize(buffer, false, featureSet)
        StringSerializerAscii.serialize(buffer, data.type.typeName, featureSet)
        data.serialize(buffer, featureSet)
      }
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): QVariant_ {
    val rawType = IntSerializer.deserialize(buffer, featureSet)
    val qtType = QtType.of(rawType)
      ?: throw NoSerializerForTypeException.Qt(rawType, null)
    // isNull, but we ignore it as it has no meaning
    BoolSerializer.deserialize(buffer, featureSet)

    return if (qtType == QtType.UserType) {
      val name = StringSerializerAscii.deserialize(buffer, featureSet)
      val quasselType = QuasselType.of(name)
        ?: throw NoSerializerForTypeException.Quassel(qtType.id, name)
      deserialize(quasselType, buffer, featureSet)
    } else {
      deserialize(qtType, buffer, featureSet)
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun deserialize(type: QtType, buffer: ByteBuffer, featureSet: FeatureSet): QVariant_ {
    val serializer = type.serializer
      as? PrimitiveSerializer<Any>
      ?: throw NoSerializerForTypeException.Qt(type)
    val value = serializer.deserialize(buffer, featureSet)
    return QVariant.Typed(value, type, serializer)
  }

  @Suppress("UNCHECKED_CAST")
  private fun deserialize(type: QuasselType, buffer: ByteBuffer, featureSet: FeatureSet): QVariant_ {
    val serializer = type.serializer
      as? PrimitiveSerializer<Any>
      ?: throw NoSerializerForTypeException.Quassel(type)
    val value = serializer.deserialize(buffer, featureSet)
    return QVariant.Custom(value, type, serializer)
  }
}
