/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.handshake

import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.handshakeSerializerTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("HandshakeSerializerTest")
class ClientLoginAckSerializerTest {
  @Test
  fun testSimple() = handshakeSerializerTest(
    HandshakeMessage.ClientLoginAck,
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x07u, 0x4Du, 0x73u, 0x67u,
      0x54u, 0x79u, 0x70u, 0x65u, 0x00u, 0x00u, 0x00u, 0x0Au, 0x00u, 0x00u, 0x00u, 0x00u, 0x1Cu, 0x00u, 0x43u, 0x00u,
      0x6Cu, 0x00u, 0x69u, 0x00u, 0x65u, 0x00u, 0x6Eu, 0x00u, 0x74u, 0x00u, 0x4Cu, 0x00u, 0x6Fu, 0x00u, 0x67u, 0x00u,
      0x69u, 0x00u, 0x6Eu, 0x00u, 0x41u, 0x00u, 0x63u, 0x00u, 0x6Bu,
    )
  )
}
