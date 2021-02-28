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

import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.network.NetworkInfo
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QuasselSerializerTest")
class NetworkInfoSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      NetworkInfoSerializer,
      QuasselType.NetworkInfo.serializer<NetworkInfo>(),
    )
  }

  @Test
  fun testEmptyMap() = primitiveSerializerTest(
    NetworkInfoSerializer,
    NetworkInfo(),
    byteBufferOf(
      // no elements
      0x00u, 0x00u, 0x00u, 0x00u,
    ),
    featureSets = emptyList(),
    serializeFeatureSet = null
  )

  @Test
  fun testNormal() = primitiveSerializerTest(
    NetworkInfoSerializer,
    NetworkInfo(
      networkId = NetworkId(4),
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x19u, 0x00u, 0x00u, 0x00u, 0x12u, 0x00u, 0x4Eu, 0x00u, 0x65u, 0x00u, 0x74u, 0x00u, 0x77u,
      0x00u, 0x6Fu, 0x00u, 0x72u, 0x00u, 0x6Bu, 0x00u, 0x49u, 0x00u, 0x64u, 0x00u, 0x00u, 0x00u, 0x7Fu, 0x00u, 0x00u,
      0x00u, 0x00u, 0x0Au, 0x4Eu, 0x65u, 0x74u, 0x77u, 0x6Fu, 0x72u, 0x6Bu, 0x49u, 0x64u, 0x00u, 0x00u, 0x00u, 0x00u,
      0x04u, 0x00u, 0x00u, 0x00u, 0x16u, 0x00u, 0x4Eu, 0x00u, 0x65u, 0x00u, 0x74u, 0x00u, 0x77u, 0x00u, 0x6Fu, 0x00u,
      0x72u, 0x00u, 0x6Bu, 0x00u, 0x4Eu, 0x00u, 0x61u, 0x00u, 0x6Du, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x10u, 0x00u, 0x49u, 0x00u, 0x64u, 0x00u, 0x65u, 0x00u, 0x6Eu,
      0x00u, 0x74u, 0x00u, 0x69u, 0x00u, 0x74u, 0x00u, 0x79u, 0x00u, 0x00u, 0x00u, 0x7Fu, 0x00u, 0x00u, 0x00u, 0x00u,
      0x0Bu, 0x49u, 0x64u, 0x65u, 0x6Eu, 0x74u, 0x69u, 0x74u, 0x79u, 0x49u, 0x64u, 0x00u, 0xFFu, 0xFFu, 0xFFu, 0xFFu,
      0x00u, 0x00u, 0x00u, 0x24u, 0x00u, 0x55u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x43u, 0x00u, 0x75u, 0x00u, 0x73u,
      0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x6Du, 0x00u, 0x45u, 0x00u, 0x6Eu, 0x00u, 0x63u, 0x00u, 0x6Fu, 0x00u, 0x64u,
      0x00u, 0x69u, 0x00u, 0x6Eu, 0x00u, 0x67u, 0x00u, 0x73u, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u, 0x1Cu, 0x00u, 0x43u, 0x00u, 0x6Fu, 0x00u, 0x64u, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x46u, 0x00u, 0x6Fu,
      0x00u, 0x72u, 0x00u, 0x53u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x76u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x00u,
      0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x05u, 0x55u, 0x54u, 0x46u, 0x5Fu, 0x38u, 0x00u, 0x00u, 0x00u, 0x20u,
      0x00u, 0x43u, 0x00u, 0x6Fu, 0x00u, 0x64u, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x46u, 0x00u, 0x6Fu, 0x00u, 0x72u,
      0x00u, 0x45u, 0x00u, 0x6Eu, 0x00u, 0x63u, 0x00u, 0x6Fu, 0x00u, 0x64u, 0x00u, 0x69u, 0x00u, 0x6Eu, 0x00u, 0x67u,
      0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x05u, 0x55u, 0x54u, 0x46u, 0x5Fu, 0x38u, 0x00u, 0x00u,
      0x00u, 0x20u, 0x00u, 0x43u, 0x00u, 0x6Fu, 0x00u, 0x64u, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x46u, 0x00u, 0x6Fu,
      0x00u, 0x72u, 0x00u, 0x44u, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x6Fu, 0x00u, 0x64u, 0x00u, 0x69u, 0x00u, 0x6Eu,
      0x00u, 0x67u, 0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x05u, 0x55u, 0x54u, 0x46u, 0x5Fu, 0x38u,
      0x00u, 0x00u, 0x00u, 0x14u, 0x00u, 0x53u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x76u, 0x00u, 0x65u, 0x00u, 0x72u,
      0x00u, 0x4Cu, 0x00u, 0x69u, 0x00u, 0x73u, 0x00u, 0x74u, 0x00u, 0x00u, 0x00u, 0x09u, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x1Eu, 0x00u, 0x55u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x52u, 0x00u, 0x61u, 0x00u,
      0x6Eu, 0x00u, 0x64u, 0x00u, 0x6Fu, 0x00u, 0x6Du, 0x00u, 0x53u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x76u, 0x00u,
      0x65u, 0x00u, 0x72u, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x0Eu, 0x00u, 0x50u, 0x00u,
      0x65u, 0x00u, 0x72u, 0x00u, 0x66u, 0x00u, 0x6Fu, 0x00u, 0x72u, 0x00u, 0x6Du, 0x00u, 0x00u, 0x00u, 0x0Bu, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x1Eu, 0x00u, 0x55u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x41u,
      0x00u, 0x75u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x49u, 0x00u, 0x64u, 0x00u, 0x65u, 0x00u, 0x6Eu, 0x00u, 0x74u,
      0x00u, 0x69u, 0x00u, 0x66u, 0x00u, 0x79u, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x26u,
      0x00u, 0x41u, 0x00u, 0x75u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x49u, 0x00u, 0x64u, 0x00u, 0x65u, 0x00u, 0x6Eu,
      0x00u, 0x74u, 0x00u, 0x69u, 0x00u, 0x66u, 0x00u, 0x79u, 0x00u, 0x53u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x76u,
      0x00u, 0x69u, 0x00u, 0x63u, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u, 0x00u, 0x28u, 0x00u, 0x41u, 0x00u, 0x75u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x49u, 0x00u, 0x64u, 0x00u,
      0x65u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x69u, 0x00u, 0x66u, 0x00u, 0x79u, 0x00u, 0x50u, 0x00u, 0x61u, 0x00u,
      0x73u, 0x00u, 0x73u, 0x00u, 0x77u, 0x00u, 0x6Fu, 0x00u, 0x72u, 0x00u, 0x64u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x0Eu, 0x00u, 0x55u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x53u,
      0x00u, 0x61u, 0x00u, 0x73u, 0x00u, 0x6Cu, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x16u,
      0x00u, 0x53u, 0x00u, 0x61u, 0x00u, 0x73u, 0x00u, 0x6Cu, 0x00u, 0x41u, 0x00u, 0x63u, 0x00u, 0x63u, 0x00u, 0x6Fu,
      0x00u, 0x75u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u, 0x00u, 0x18u, 0x00u, 0x53u, 0x00u, 0x61u, 0x00u, 0x73u, 0x00u, 0x6Cu, 0x00u, 0x50u, 0x00u, 0x61u, 0x00u,
      0x73u, 0x00u, 0x73u, 0x00u, 0x77u, 0x00u, 0x6Fu, 0x00u, 0x72u, 0x00u, 0x64u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x20u, 0x00u, 0x55u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x41u,
      0x00u, 0x75u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x6Fu, 0x00u, 0x6Eu,
      0x00u, 0x6Eu, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x74u, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x01u, 0x00u, 0x00u,
      0x00u, 0x2Au, 0x00u, 0x41u, 0x00u, 0x75u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x63u,
      0x00u, 0x6Fu, 0x00u, 0x6Eu, 0x00u, 0x6Eu, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x74u, 0x00u, 0x49u, 0x00u, 0x6Eu,
      0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x76u, 0x00u, 0x61u, 0x00u, 0x6Cu, 0x00u, 0x00u, 0x00u, 0x03u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x28u, 0x00u, 0x41u, 0x00u, 0x75u, 0x00u, 0x74u, 0x00u,
      0x6Fu, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x6Fu, 0x00u, 0x6Eu, 0x00u, 0x6Eu, 0x00u, 0x65u, 0x00u,
      0x63u, 0x00u, 0x74u, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x74u, 0x00u, 0x72u, 0x00u, 0x69u, 0x00u, 0x65u, 0x00u,
      0x73u, 0x00u, 0x00u, 0x00u, 0x85u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x32u, 0x00u, 0x55u, 0x00u, 0x6Eu,
      0x00u, 0x6Cu, 0x00u, 0x69u, 0x00u, 0x6Du, 0x00u, 0x69u, 0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x64u, 0x00u, 0x52u,
      0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x6Fu, 0x00u, 0x6Eu, 0x00u, 0x6Eu, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x74u,
      0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x74u, 0x00u, 0x72u, 0x00u, 0x69u, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x00u,
      0x00u, 0x01u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x1Cu, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x6Au, 0x00u, 0x6Fu,
      0x00u, 0x69u, 0x00u, 0x6Eu, 0x00u, 0x43u, 0x00u, 0x68u, 0x00u, 0x61u, 0x00u, 0x6Eu, 0x00u, 0x6Eu, 0x00u, 0x65u,
      0x00u, 0x6Cu, 0x00u, 0x73u, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x28u, 0x00u, 0x55u,
      0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x43u, 0x00u, 0x75u, 0x00u, 0x73u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x6Du,
      0x00u, 0x4Du, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x73u, 0x00u, 0x61u, 0x00u, 0x67u, 0x00u, 0x65u, 0x00u, 0x52u,
      0x00u, 0x61u, 0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x28u,
      0x00u, 0x4Du, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x73u, 0x00u, 0x61u, 0x00u, 0x67u, 0x00u, 0x65u, 0x00u, 0x52u,
      0x00u, 0x61u, 0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x42u, 0x00u, 0x75u, 0x00u, 0x72u, 0x00u, 0x73u, 0x00u, 0x74u,
      0x00u, 0x53u, 0x00u, 0x69u, 0x00u, 0x7Au, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x03u, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x20u, 0x00u, 0x4Du, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x73u, 0x00u, 0x61u, 0x00u,
      0x67u, 0x00u, 0x65u, 0x00u, 0x52u, 0x00u, 0x61u, 0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x44u, 0x00u, 0x65u, 0x00u,
      0x6Cu, 0x00u, 0x61u, 0x00u, 0x79u, 0x00u, 0x00u, 0x00u, 0x03u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u, 0x28u, 0x00u, 0x55u, 0x00u, 0x6Eu, 0x00u, 0x6Cu, 0x00u, 0x69u, 0x00u, 0x6Du, 0x00u, 0x69u, 0x00u, 0x74u,
      0x00u, 0x65u, 0x00u, 0x64u, 0x00u, 0x4Du, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x73u, 0x00u, 0x61u, 0x00u, 0x67u,
      0x00u, 0x65u, 0x00u, 0x52u, 0x00u, 0x61u, 0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x01u, 0x00u, 0x00u,
    )
  )
}
