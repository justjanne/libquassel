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
import org.threeten.bp.Instant

@Tag("SignalProxySerializerTest")
class HeartBeatSerializerTest {
  @Test
  fun testEmptyMap() = signalProxySerializerTest(
    SignalProxyMessage.HeartBeat(
      Instant.EPOCH
    ),
    byteBufferOf(
      // 4 elements
      0x00u, 0x00u, 0x00u, 0x01u,
      // int
      0x00u, 0x00u, 0x00u, 0x02u,
      0x00u,
      // HeartBeat
      0x00u, 0x00u, 0x00u, 0x05u,
    ),
    featureSets = emptyList(),
    serializeFeatureSet = null
  )

  @Test
  fun testSimple() = signalProxySerializerTest(
    SignalProxyMessage.HeartBeat(
      Instant.EPOCH
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x05u, 0x00u, 0x00u, 0x00u,
      0x10u, 0x00u, 0x00u, 0x25u, 0x3Du, 0x8Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x02u,
    )
  )

  @Test
  fun testRealistic() = signalProxySerializerTest(
    SignalProxyMessage.HeartBeat(
      Instant.ofEpochMilli(1614520296337)
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x05u, 0x00u, 0x00u, 0x00u,
      0x10u, 0x00u, 0x00u, 0x25u, 0x86u, 0x8Au, 0x02u, 0xF9u, 0x5Bu, 0x91u, 0x02u,
    )
  )
}
