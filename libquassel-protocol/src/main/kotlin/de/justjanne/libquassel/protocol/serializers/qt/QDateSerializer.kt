/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.JulianFields
import java.nio.ByteBuffer

/**
 * Serializer for [LocalDate]
 */
object QDateSerializer : PrimitiveSerializer<LocalDate> {
  override val javaType: Class<out LocalDate> = LocalDate::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: LocalDate, featureSet: FeatureSet) {
    val julianDay = data.getLong(JulianFields.JULIAN_DAY).toInt()
    IntSerializer.serialize(buffer, julianDay, featureSet)
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): LocalDate {
    val julianDay = IntSerializer.deserialize(buffer, featureSet).toLong()
    return LocalDate.ofEpochDay(0).with(JulianFields.JULIAN_DAY, julianDay)
  }
}
