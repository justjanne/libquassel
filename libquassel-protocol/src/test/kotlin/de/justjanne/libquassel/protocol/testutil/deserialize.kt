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
package de.justjanne.libquassel.protocol.testutil

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.serializers.HandshakeSerializer
import de.justjanne.libquassel.protocol.serializers.Serializer
import de.justjanne.libquassel.protocol.serializers.qt.HandshakeMapSerializer
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import java.nio.ByteBuffer

fun <T> deserialize(
  serializer: Serializer<T>,
  buffer: ByteBuffer,
  featureSet: FeatureSet = FeatureSet.all()
): T {
  val result = serializer.deserialize(buffer, featureSet)
  assertEquals(0, buffer.remaining())
  return result
}

fun <T> testDeserialize(
  serializer: Serializer<T>,
  matcher: Matcher<in T>,
  buffer: ByteBuffer,
  featureSet: FeatureSet = FeatureSet.all()
) {
  val after = deserialize(serializer, buffer, featureSet)
  assertThat(after, matcher)
}

fun <T> testDeserialize(
  serializer: Serializer<T>,
  data: T,
  buffer: ByteBuffer,
  featureSet: FeatureSet = FeatureSet.all()
) {
  val after = deserialize(serializer, buffer, featureSet)
  assertEquals(data, after)
}

fun <T> testDeserialize(
  serializer: HandshakeSerializer<T>,
  matcher: Matcher<in T>,
  buffer: ByteBuffer,
  featureSet: FeatureSet = FeatureSet.all()
) {
  val map = deserialize(HandshakeMapSerializer, buffer, featureSet)
  val after = serializer.deserialize(map)
  assertThat(after, matcher)
}

fun <T> testDeserialize(
  serializer: HandshakeSerializer<T>,
  data: T,
  buffer: ByteBuffer,
  featureSet: FeatureSet = FeatureSet.all()
) {
  val map = deserialize(HandshakeMapSerializer, buffer, featureSet)
  val after = serializer.deserialize(map)
  assertEquals(data, after)
}
