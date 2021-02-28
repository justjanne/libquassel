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
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import org.threeten.bp.LocalTime
import java.nio.ByteBuffer

/**
 * Serializer for [LocalTime]
 */
object QTimeSerializer : PrimitiveSerializer<LocalTime> {
  override val javaType: Class<out LocalTime> = LocalTime::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: LocalTime, featureSet: FeatureSet) {
    val millisecondOfDay = (data.toNanoOfDay() / 1_000_000).toInt()
    IntSerializer.serialize(buffer, millisecondOfDay, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): LocalTime {
    val millisecondOfDay = IntSerializer.deserialize(buffer, featureSet).toLong()
    return LocalTime.ofNanoOfDay(millisecondOfDay * 1_000_000)
  }
}
