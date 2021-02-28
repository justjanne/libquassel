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
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.IrcUser
import de.justjanne.libquassel.protocol.syncables.state.IrcUserState
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.matchers.MapMatcher
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import de.justjanne.libquassel.protocol.variant.QVariantMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QuasselSerializerTest")
class IrcUserSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      IrcUserSerializer,
      QuasselType.IrcUser.serializer<QVariantMap>(),
    )
  }

  @Test
  fun testEmptyMap() = primitiveSerializerTest(
    IrcUserSerializer,
    emptyMap(),
    byteBufferOf(
      // no elements
      0x00u, 0x00u, 0x00u, 0x00u,
    ),
    featureSets = emptyList(),
    serializeFeatureSet = null
  )

  @Test
  fun testNormal() = primitiveSerializerTest(
    IrcUserSerializer,
    IrcUser(
      state = IrcUserState(
        network = NetworkId(4),
        nick = "AzureDiamond",
        user = "~azure",
        host = "127.0.0.1"
      )
    ).toVariantMap(),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x12u, 0x00u, 0x00u, 0x00u, 0x08u, 0x00u, 0x6Eu, 0x00u, 0x69u, 0x00u, 0x63u, 0x00u, 0x6Bu,
      0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x18u, 0x00u, 0x41u, 0x00u, 0x7Au, 0x00u, 0x75u, 0x00u,
      0x72u, 0x00u, 0x65u, 0x00u, 0x44u, 0x00u, 0x69u, 0x00u, 0x61u, 0x00u, 0x6Du, 0x00u, 0x6Fu, 0x00u, 0x6Eu, 0x00u,
      0x64u, 0x00u, 0x00u, 0x00u, 0x08u, 0x00u, 0x75u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x00u, 0x00u,
      0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x7Eu, 0x00u, 0x61u, 0x00u, 0x7Au, 0x00u, 0x75u, 0x00u, 0x72u,
      0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x08u, 0x00u, 0x68u, 0x00u, 0x6Fu, 0x00u, 0x73u, 0x00u, 0x74u, 0x00u, 0x00u,
      0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x12u, 0x00u, 0x31u, 0x00u, 0x32u, 0x00u, 0x37u, 0x00u, 0x2Eu, 0x00u,
      0x30u, 0x00u, 0x2Eu, 0x00u, 0x30u, 0x00u, 0x2Eu, 0x00u, 0x31u, 0x00u, 0x00u, 0x00u, 0x10u, 0x00u, 0x72u, 0x00u,
      0x65u, 0x00u, 0x61u, 0x00u, 0x6Cu, 0x00u, 0x4Eu, 0x00u, 0x61u, 0x00u, 0x6Du, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u,
      0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x0Eu, 0x00u, 0x61u, 0x00u, 0x63u, 0x00u, 0x63u,
      0x00u, 0x6Fu, 0x00u, 0x75u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x08u, 0x00u, 0x61u, 0x00u, 0x77u, 0x00u, 0x61u, 0x00u, 0x79u, 0x00u, 0x00u, 0x00u,
      0x01u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x16u, 0x00u, 0x61u, 0x00u, 0x77u, 0x00u, 0x61u, 0x00u, 0x79u, 0x00u,
      0x4Du, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x73u, 0x00u, 0x61u, 0x00u, 0x67u, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u,
      0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x10u, 0x00u, 0x69u, 0x00u, 0x64u, 0x00u, 0x6Cu,
      0x00u, 0x65u, 0x00u, 0x54u, 0x00u, 0x69u, 0x00u, 0x6Du, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x10u, 0x00u, 0x00u,
      0x25u, 0x3Du, 0x8Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x12u, 0x00u, 0x6Cu, 0x00u, 0x6Fu,
      0x00u, 0x67u, 0x00u, 0x69u, 0x00u, 0x6Eu, 0x00u, 0x54u, 0x00u, 0x69u, 0x00u, 0x6Du, 0x00u, 0x65u, 0x00u, 0x00u,
      0x00u, 0x10u, 0x00u, 0x00u, 0x25u, 0x3Du, 0x8Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x0Cu,
      0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x76u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x00u, 0x00u, 0x0Au,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x16u, 0x00u, 0x69u, 0x00u, 0x72u, 0x00u, 0x63u, 0x00u,
      0x4Fu, 0x00u, 0x70u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x61u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x72u, 0x00u,
      0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x1Eu, 0x00u, 0x6Cu, 0x00u, 0x61u,
      0x00u, 0x73u, 0x00u, 0x74u, 0x00u, 0x41u, 0x00u, 0x77u, 0x00u, 0x61u, 0x00u, 0x79u, 0x00u, 0x4Du, 0x00u, 0x65u,
      0x00u, 0x73u, 0x00u, 0x73u, 0x00u, 0x61u, 0x00u, 0x67u, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x26u, 0x00u, 0x6Cu, 0x00u, 0x61u, 0x00u, 0x73u, 0x00u, 0x74u, 0x00u,
      0x41u, 0x00u, 0x77u, 0x00u, 0x61u, 0x00u, 0x79u, 0x00u, 0x4Du, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x73u, 0x00u,
      0x61u, 0x00u, 0x67u, 0x00u, 0x65u, 0x00u, 0x54u, 0x00u, 0x69u, 0x00u, 0x6Du, 0x00u, 0x65u, 0x00u, 0x00u, 0x00u,
      0x10u, 0x00u, 0x00u, 0x25u, 0x3Du, 0x8Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x22u, 0x00u,
      0x77u, 0x00u, 0x68u, 0x00u, 0x6Fu, 0x00u, 0x69u, 0x00u, 0x73u, 0x00u, 0x53u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u,
      0x76u, 0x00u, 0x69u, 0x00u, 0x63u, 0x00u, 0x65u, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x70u, 0x00u, 0x6Cu, 0x00u,
      0x79u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x12u, 0x00u, 0x73u,
      0x00u, 0x75u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x48u, 0x00u, 0x6Fu, 0x00u, 0x73u, 0x00u, 0x74u,
      0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x12u, 0x00u, 0x65u, 0x00u,
      0x6Eu, 0x00u, 0x63u, 0x00u, 0x72u, 0x00u, 0x79u, 0x00u, 0x70u, 0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x64u, 0x00u,
      0x00u, 0x00u, 0x01u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x10u, 0x00u, 0x63u, 0x00u, 0x68u, 0x00u, 0x61u, 0x00u,
      0x6Eu, 0x00u, 0x6Eu, 0x00u, 0x65u, 0x00u, 0x6Cu, 0x00u, 0x73u, 0x00u, 0x00u, 0x00u, 0x0Bu, 0x00u, 0x00u, 0x00u,
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x12u, 0x00u, 0x75u, 0x00u, 0x73u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x4Du,
      0x00u, 0x6Fu, 0x00u, 0x64u, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u,
      0x00u,
    ),
    matcher = ::MapMatcher
  )
}
