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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.signalProxySerializerTest
import de.justjanne.libquassel.protocol.variant.QVariant_
import de.justjanne.libquassel.protocol.variant.qVariant
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("SignalProxySerializerTest")
class RpcSerializerTest {
  @Test
  fun testEmptyMap() = signalProxySerializerTest(
    SignalProxyMessage.Rpc(
      slotName = "",
      params = emptyList()
    ),
    byteBufferOf(
      // 4 elements
      0x00u, 0x00u, 0x00u, 0x01u,
      // int
      0x00u, 0x00u, 0x00u, 0x02u,
      0x00u,
      // Rpc
      0x00u, 0x00u, 0x00u, 0x02u,
    ),
    featureSets = emptyList(),
    serializeFeatureSet = null
  )

  @Test
  fun testSimple() = signalProxySerializerTest(
    SignalProxyMessage.Rpc(
      slotName = "",
      params = emptyList()
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u,
      0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
    )
  )

  @Test
  fun testRealistic() {
    signalProxySerializerTest(
      SignalProxyMessage.Rpc(
        slotName = "2createIdentity(Identity,QVariantMap)",
        params = listOf(
          qVariant(
            emptyMap(),
            QuasselType.Identity
          ),
          qVariant(
            emptyMap<String, QVariant_>(),
            QtType.QVariantMap
          )
        )
      ),
      byteBufferOf(
        0x00u, 0x00u, 0x00u, 0x04u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u,
        0x0Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x25u, 0x32u, 0x63u, 0x72u, 0x65u, 0x61u, 0x74u, 0x65u, 0x49u, 0x64u, 0x65u,
        0x6Eu, 0x74u, 0x69u, 0x74u, 0x79u, 0x28u, 0x49u, 0x64u, 0x65u, 0x6Eu, 0x74u, 0x69u, 0x74u, 0x79u, 0x2Cu, 0x51u,
        0x56u, 0x61u, 0x72u, 0x69u, 0x61u, 0x6Eu, 0x74u, 0x4Du, 0x61u, 0x70u, 0x29u, 0x00u, 0x00u, 0x00u, 0x7Fu, 0x00u,
        0x00u, 0x00u, 0x00u, 0x09u, 0x49u, 0x64u, 0x65u, 0x6Eu, 0x74u, 0x69u, 0x74u, 0x79u, 0x00u, 0x00u, 0x00u, 0x00u,
        0x00u, 0x00u, 0x00u, 0x00u, 0x08u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
      )
    )
  }
}
