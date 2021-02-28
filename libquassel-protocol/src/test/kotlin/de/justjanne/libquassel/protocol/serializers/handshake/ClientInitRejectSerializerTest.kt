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
package de.justjanne.libquassel.protocol.serializers.handshake

import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.handshakeSerializerTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("HandshakeSerializerTest")
class ClientInitRejectSerializerTest {
  @Test
  fun testEmptyMap() = handshakeSerializerTest(
    HandshakeMessage.ClientInitReject(
      errorString = null
    ),
    byteBufferOf(
      // 4 elements
      0x00u, 0x00u, 0x00u, 0x02u,
      // ByteBuffer
      0x00u, 0x00u, 0x00u, 0x0Cu,
      0x00u,
      // 7 bytes
      0x00u, 0x00u, 0x00u, 0x07u,
      // MsgType
      0x4Du, 0x73u, 0x67u, 0x54u, 0x79u, 0x70u, 0x65u,
      // String
      0x00u, 0x00u, 0x00u, 0x0Au,
      0x00u,
      // 16 2-byte chars
      0x00u, 0x00u, 0x00u, 0x20u,
      // ClientInitReject
      0x00u, 0x43u, 0x00u, 0x6Cu, 0x00u, 0x69u, 0x00u, 0x65u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x49u, 0x00u, 0x6Eu,
      0x00u, 0x69u, 0x00u, 0x74u, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x6Au, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x74u,
    ),
    featureSets = emptyList(),
    serializeFeatureSet = null
  )

  @Test
  fun testEmpty() = handshakeSerializerTest(
    HandshakeMessage.ClientInitReject(
      errorString = null
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x04u, 0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x07u, 0x4Du, 0x73u, 0x67u,
      0x54u, 0x79u, 0x70u, 0x65u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x20u, 0x00u, 0x43u, 0x00u,
      0x6Cu, 0x00u, 0x69u, 0x00u, 0x65u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x49u, 0x00u, 0x6Eu, 0x00u, 0x69u, 0x00u,
      0x74u, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x6Au, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x74u, 0x00u, 0x00u, 0x00u,
      0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x05u, 0x45u, 0x72u, 0x72u, 0x6Fu, 0x72u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u,
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
    )
  )

  @Test
  fun testSimple() = handshakeSerializerTest(
    HandshakeMessage.ClientInitReject(
      errorString = "hm. I've lost a machine.. literally _lost_. it responds to ping, it works completely, I just " +
        "can't figure out where in my apartment it is."
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x04u, 0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x07u, 0x4Du, 0x73u, 0x67u,
      0x54u, 0x79u, 0x70u, 0x65u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x20u, 0x00u, 0x43u, 0x00u,
      0x6Cu, 0x00u, 0x69u, 0x00u, 0x65u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x49u, 0x00u, 0x6Eu, 0x00u, 0x69u, 0x00u,
      0x74u, 0x00u, 0x52u, 0x00u, 0x65u, 0x00u, 0x6Au, 0x00u, 0x65u, 0x00u, 0x63u, 0x00u, 0x74u, 0x00u, 0x00u, 0x00u,
      0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x05u, 0x45u, 0x72u, 0x72u, 0x6Fu, 0x72u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u,
      0x00u, 0x00u, 0x01u, 0x14u, 0x00u, 0x68u, 0x00u, 0x6Du, 0x00u, 0x2Eu, 0x00u, 0x20u, 0x00u, 0x49u, 0x00u, 0x27u,
      0x00u, 0x76u, 0x00u, 0x65u, 0x00u, 0x20u, 0x00u, 0x6Cu, 0x00u, 0x6Fu, 0x00u, 0x73u, 0x00u, 0x74u, 0x00u, 0x20u,
      0x00u, 0x61u, 0x00u, 0x20u, 0x00u, 0x6Du, 0x00u, 0x61u, 0x00u, 0x63u, 0x00u, 0x68u, 0x00u, 0x69u, 0x00u, 0x6Eu,
      0x00u, 0x65u, 0x00u, 0x2Eu, 0x00u, 0x2Eu, 0x00u, 0x20u, 0x00u, 0x6Cu, 0x00u, 0x69u, 0x00u, 0x74u, 0x00u, 0x65u,
      0x00u, 0x72u, 0x00u, 0x61u, 0x00u, 0x6Cu, 0x00u, 0x6Cu, 0x00u, 0x79u, 0x00u, 0x20u, 0x00u, 0x5Fu, 0x00u, 0x6Cu,
      0x00u, 0x6Fu, 0x00u, 0x73u, 0x00u, 0x74u, 0x00u, 0x5Fu, 0x00u, 0x2Eu, 0x00u, 0x20u, 0x00u, 0x69u, 0x00u, 0x74u,
      0x00u, 0x20u, 0x00u, 0x72u, 0x00u, 0x65u, 0x00u, 0x73u, 0x00u, 0x70u, 0x00u, 0x6Fu, 0x00u, 0x6Eu, 0x00u, 0x64u,
      0x00u, 0x73u, 0x00u, 0x20u, 0x00u, 0x74u, 0x00u, 0x6Fu, 0x00u, 0x20u, 0x00u, 0x70u, 0x00u, 0x69u, 0x00u, 0x6Eu,
      0x00u, 0x67u, 0x00u, 0x2Cu, 0x00u, 0x20u, 0x00u, 0x69u, 0x00u, 0x74u, 0x00u, 0x20u, 0x00u, 0x77u, 0x00u, 0x6Fu,
      0x00u, 0x72u, 0x00u, 0x6Bu, 0x00u, 0x73u, 0x00u, 0x20u, 0x00u, 0x63u, 0x00u, 0x6Fu, 0x00u, 0x6Du, 0x00u, 0x70u,
      0x00u, 0x6Cu, 0x00u, 0x65u, 0x00u, 0x74u, 0x00u, 0x65u, 0x00u, 0x6Cu, 0x00u, 0x79u, 0x00u, 0x2Cu, 0x00u, 0x20u,
      0x00u, 0x49u, 0x00u, 0x20u, 0x00u, 0x6Au, 0x00u, 0x75u, 0x00u, 0x73u, 0x00u, 0x74u, 0x00u, 0x20u, 0x00u, 0x63u,
      0x00u, 0x61u, 0x00u, 0x6Eu, 0x00u, 0x27u, 0x00u, 0x74u, 0x00u, 0x20u, 0x00u, 0x66u, 0x00u, 0x69u, 0x00u, 0x67u,
      0x00u, 0x75u, 0x00u, 0x72u, 0x00u, 0x65u, 0x00u, 0x20u, 0x00u, 0x6Fu, 0x00u, 0x75u, 0x00u, 0x74u, 0x00u, 0x20u,
      0x00u, 0x77u, 0x00u, 0x68u, 0x00u, 0x65u, 0x00u, 0x72u, 0x00u, 0x65u, 0x00u, 0x20u, 0x00u, 0x69u, 0x00u, 0x6Eu,
      0x00u, 0x20u, 0x00u, 0x6Du, 0x00u, 0x79u, 0x00u, 0x20u, 0x00u, 0x61u, 0x00u, 0x70u, 0x00u, 0x61u, 0x00u, 0x72u,
      0x00u, 0x74u, 0x00u, 0x6Du, 0x00u, 0x65u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x20u, 0x00u, 0x69u, 0x00u, 0x74u,
      0x00u, 0x20u, 0x00u, 0x69u, 0x00u, 0x73u, 0x00u, 0x2Eu,
    )
  )
}
