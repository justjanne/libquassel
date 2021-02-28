/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.signalproxy

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.signalProxySerializerTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("SignalProxySerializerTest")
class InitRequestSerializerTest {
  @Test
  fun testEmptyMap() = signalProxySerializerTest(
    SignalProxyMessage.InitRequest(
      className = "",
      objectName = ""
    ),
    byteBufferOf(
      // 4 elements
      0x00u, 0x00u, 0x00u, 0x01u,
      // int
      0x00u, 0x00u, 0x00u, 0x02u,
      0x00u,
      // Rpc
      0x00u, 0x00u, 0x00u, 0x03u,
    ),
    featureSets = emptyList(),
    serializeFeatureSet = null
  )

  @Test
  fun testSimple() = signalProxySerializerTest(
    SignalProxyMessage.InitRequest(
      className = "",
      objectName = ""
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x03u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x03u, 0x00u, 0x00u, 0x00u,
      0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    )
  )

  @Test
  fun testRealistic() {
    signalProxySerializerTest(
      SignalProxyMessage.InitRequest(
        className = "Network",
        objectName = "4"
      ),
      byteBufferOf(
        0x00u, 0x00u, 0x00u, 0x03u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x03u, 0x00u, 0x00u, 0x00u,
        0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x07u, 0x4Eu, 0x65u, 0x74u, 0x77u, 0x6Fu, 0x72u, 0x6Bu, 0x00u, 0x00u, 0x00u,
        0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x01u, 0x34u,
      )
    )
  }
}
