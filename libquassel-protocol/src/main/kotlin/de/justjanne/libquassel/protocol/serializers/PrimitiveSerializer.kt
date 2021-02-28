/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.io.ChainedByteBuffer
import java.nio.ByteBuffer

/**
 * Interface describing a generic quassel protocol serializer.
 *
 * See [QDataStream Documentation](https://doc.qt.io/qt-5/qdatastream.html)
 */
interface PrimitiveSerializer<T> {
  /**
   * The underlying Java type this Serializer can (de-)serialize.
   * Used for type-safe serializer autodiscovery.
   */
  val javaType: Class<out T>
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
