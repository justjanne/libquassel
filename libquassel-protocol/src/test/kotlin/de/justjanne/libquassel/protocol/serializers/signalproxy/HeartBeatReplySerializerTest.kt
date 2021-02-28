/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
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
import org.threeten.bp.Instant

@Tag("SignalProxySerializerTest")
class HeartBeatReplySerializerTest {
  @Test
  fun testEmptyMap() = signalProxySerializerTest(
    SignalProxyMessage.HeartBeatReply(
      Instant.EPOCH
    ),
    byteBufferOf(
      // 4 elements
      0x00u, 0x00u, 0x00u, 0x01u,
      // int
      0x00u, 0x00u, 0x00u, 0x02u,
      0x00u,
      // HeartBeatReply
      0x00u, 0x00u, 0x00u, 0x06u,
    ),
    featureSets = emptyList(),
    serializeFeatureSet = null
  )

  @Test
  fun testSimple() = signalProxySerializerTest(
    SignalProxyMessage.HeartBeatReply(
      Instant.EPOCH
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x06u, 0x00u, 0x00u, 0x00u,
      0x10u, 0x00u, 0x00u, 0x25u, 0x3Du, 0x8Cu, 0x00u, 0x00u, 0x00u, 0x00u, 0x02u,
    )
  )

  @Test
  fun testRealistic() = signalProxySerializerTest(
    SignalProxyMessage.HeartBeatReply(
      Instant.ofEpochMilli(1614520296337)
    ),
    byteBufferOf(
      0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x02u, 0x00u, 0x00u, 0x00u, 0x00u, 0x06u, 0x00u, 0x00u, 0x00u,
      0x10u, 0x00u, 0x00u, 0x25u, 0x86u, 0x8Au, 0x02u, 0xF9u, 0x5Bu, 0x91u, 0x02u,
    )
  )
}
