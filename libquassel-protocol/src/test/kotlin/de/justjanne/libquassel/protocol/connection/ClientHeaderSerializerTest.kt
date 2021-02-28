/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.connection

import de.justjanne.bitflags.of
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.serializerTest
import org.junit.jupiter.api.Test

class ClientHeaderSerializerTest {
  @Test
  fun testQuasseldroid() = serializerTest(
    ClientHeaderSerializer,
    ClientHeader(
      features = ProtocolFeature.of(
        ProtocolFeature.TLS,
        ProtocolFeature.Compression,
      ),
      versions = listOf(
        ProtocolMeta(
          version = ProtocolVersion.Datastream,
          data = 0x0000u,
        )
      )
    ),
    byteBufferOf(
      0x42u, 0xb3u, 0x3fu, 0x03u,
      0x80u, 0x00u, 0x00u, 0x02u
    )
  )

  @Test
  fun testQuasselClient() = serializerTest(
    ClientHeaderSerializer,
    ClientHeader(
      features = ProtocolFeature.of(
        ProtocolFeature.TLS,
        ProtocolFeature.Compression,
      ),
      versions = listOf(
        ProtocolMeta(
          version = ProtocolVersion.Legacy,
          data = 0x0000u,
        ),
        ProtocolMeta(
          version = ProtocolVersion.Datastream,
          data = 0x0000u,
        )
      )
    ),
    byteBufferOf(
      0x42u, 0xb3u, 0x3fu, 0x03u,
      0x00u, 0x00u, 0x00u, 0x01u,
      0x80u, 0x00u, 0x00u, 0x02u
    )
  )

  @Test
  fun testDebugClient() = serializerTest(
    ClientHeaderSerializer,
    ClientHeader(
      features = ProtocolFeature.of(),
      versions = listOf(
        ProtocolMeta(
          version = ProtocolVersion.Legacy,
          data = 0x0000u,
        ),
        ProtocolMeta(
          version = ProtocolVersion.Datastream,
          data = 0x0000u,
        )
      )
    ),
    byteBufferOf(
      0x42u, 0xb3u, 0x3fu, 0x00u,
      0x00u, 0x00u, 0x00u, 0x01u,
      0x80u, 0x00u, 0x00u, 0x02u
    )
  )
}
