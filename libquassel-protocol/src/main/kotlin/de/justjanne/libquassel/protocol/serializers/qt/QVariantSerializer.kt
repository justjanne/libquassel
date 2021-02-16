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
