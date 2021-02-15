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

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import java.nio.ByteBuffer

/**
 * Interface describing a generic quassel protocol serializer.
 *
 * Further specialized into [QtSerializer] and  [QuasselSerializer]
 */
interface Serializer<T> {
  /**
   * Serialize data with the Quassel protocol to a buffer
   * @param buffer target buffer to serialize to
   * @param data value to serialize
   * @param featureSet features to use when serializing, usually the featureset
   * of the currently negotiated connection
   */
  fun serialize(buffer: ChainedByteBuffer, data: T, featureSet: FeatureSet)

  /**
   * Deserialize Quassel protocol data from a buffer
   * @param buffer source buffer to deserialize from
   * @param featureSet features to use when deserializing, usually the
   * featureset of the currently negotiated connection
   */
  fun deserialize(buffer: ByteBuffer, featureSet: FeatureSet): T
}
