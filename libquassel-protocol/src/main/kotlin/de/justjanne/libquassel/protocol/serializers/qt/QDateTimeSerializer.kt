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
import de.justjanne.libquassel.protocol.models.TimeSpec
import de.justjanne.libquassel.protocol.serializers.PrimitiveSerializer
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.Temporal
import java.nio.ByteBuffer

/**
 * Serializer for temporal types such as [Instant], [ZonedDateTime], [OffsetDateTime] or [LocalDateTime]
 */
object QDateTimeSerializer : PrimitiveSerializer<Temporal> {
  override val javaType: Class<out Temporal> = Temporal::class.java

  override fun serialize(buffer: ChainedByteBuffer, data: Temporal, featureSet: FeatureSet) {
    fun serialize(data: LocalDateTime, timeSpec: TimeSpec, offset: ZoneOffset?) {
      QDateSerializer.serialize(buffer, data.toLocalDate(), featureSet)
      QTimeSerializer.serialize(buffer, data.toLocalTime(), featureSet)
      ByteSerializer.serialize(buffer, timeSpec.value, featureSet)
      if (offset != null) {
        IntSerializer.serialize(buffer, offset.totalSeconds, featureSet)
      }
    }

    when (data) {
      is LocalDateTime ->
        serialize(data, TimeSpec.LocalUnknown, null)
      is OffsetDateTime ->
        serialize(data.toLocalDateTime(), TimeSpec.OffsetFromUTC, data.offset)
      is ZonedDateTime ->
        serialize(data.toLocalDateTime(), TimeSpec.OffsetFromUTC, data.offset)
      is Instant ->
        serialize(data.atOffset(ZoneOffset.UTC).toLocalDateTime(), TimeSpec.UTC, null)
      else ->
        throw IllegalArgumentException("Unsupported Format: ${data::class.java.canonicalName}")
    }
  }

  override fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): Temporal {
    val julianDay = QDateSerializer.deserialize(buffer, featureSet)
    val localTime = QTimeSerializer.deserialize(buffer, featureSet)
    val localDateTime = LocalDateTime.of(julianDay, localTime)
    val timeSpec = TimeSpec.of(ByteSerializer.deserialize(buffer, featureSet))
      ?: TimeSpec.LocalUnknown
    return when (timeSpec) {
      TimeSpec.LocalStandard,
      TimeSpec.LocalUnknown,
      TimeSpec.LocalDST ->
        localDateTime
      TimeSpec.OffsetFromUTC ->
        localDateTime
          .atOffset(
            ZoneOffset.ofTotalSeconds(
              IntSerializer.deserialize(buffer, featureSet)
            )
          )
      TimeSpec.UTC ->
        localDateTime
          .atOffset(ZoneOffset.UTC)
          .toInstant()
    }
  }
}
