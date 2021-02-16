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
package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.quasselSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PeerPtrSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      PeerPtrSerializer,
      QuasselType.PeerPtr.serializer<ULong>(),
    )
  }

  @Test
  fun testZero() = quasselSerializerTest(
    PeerPtrSerializer,
    0uL,
    byteBufferOf(0, 0, 0, 0, 0, 0, 0, 0),
    featureSets = listOf(FeatureSet.all())
  )

  @Test
  fun testMinimal() = quasselSerializerTest(
    PeerPtrSerializer,
    ULong.MIN_VALUE,
    byteBufferOf(0, 0, 0, 0, 0, 0, 0, 0),
    featureSets = listOf(FeatureSet.all())
  )

  @Test
  fun testMaximal() = quasselSerializerTest(
    PeerPtrSerializer,
    ULong.MAX_VALUE,
    byteBufferOf(-1, -1, -1, -1, -1, -1, -1, -1),
    featureSets = listOf(FeatureSet.all())
  )

  @Test
  fun testAllOnes() = quasselSerializerTest(
    PeerPtrSerializer,
    0uL.inv(),
    byteBufferOf(-1, -1, -1, -1, -1, -1, -1, -1),
    featureSets = listOf(FeatureSet.all())
  )
}
