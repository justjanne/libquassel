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
class CoreSetupAckSerializerTest {
  @Test
  fun testSimple() = handshakeSerializerTest(
    HandshakeMessage.CoreSetupAck,
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x07u, 0x4Du, 0x73u, 0x67u,
      0x54u, 0x79u, 0x70u, 0x65u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x18u, 0x00u, 0x43u, 0x00u,
      0x6Fu, 0x00u, 0x72u, 0x00u, 0x65u, 0x00u, 0x53u, 0x00u, 0x65u, 0x00u, 0x74u, 0x00u, 0x75u, 0x00u, 0x70u, 0x00u,
      0x41u, 0x00u, 0x63u, 0x00u, 0x6Bu,
    )
  )
}
