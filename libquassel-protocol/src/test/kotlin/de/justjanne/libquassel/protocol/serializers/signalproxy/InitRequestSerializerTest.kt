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
