/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.bitflags.none
import de.justjanne.bitflags.validValues
import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.Message
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.flags.MessageFlag
import de.justjanne.libquassel.protocol.models.flags.MessageType
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.threeten.bp.Instant

@Tag("QuasselSerializerTest")
class MessageSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      MessageSerializer,
      QuasselType.Message.serializer<Message>(),
    )
  }

  @Test
  fun testEmpty() = primitiveSerializerTest(
    MessageSerializer,
    Message(
      MsgId(-1),
      Instant.EPOCH,
      MessageType.none(),
      MessageFlag.none(),
      BufferInfo(
        BufferId(-1),
        NetworkId(-1),
        BufferType.none(),
        -1,
        null
      ),
      "",
      "",
      "",
      "",
      ""
    ),
    byteBufferOf(
      // MsgId
      0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // Time
      0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u, 0x00u,
      // Type
      0x00u, 0x00u, 0x00u, 0x00u,
      // Flags
      0x00u,
      // BufferId
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // NetworkId
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // BufferType
      0x00u, 0x00u,
      // GroupId
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // Buffername
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // Sender
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // Prefixes
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // RealName
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // AvatarUrl
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // Content
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
    ),
    deserializeFeatureSet = FeatureSet.all(),
    serializeFeatureSet = null
  )

  @Test
  fun testBaseCase() = primitiveSerializerTest(
    MessageSerializer,
    Message(
      MsgId(-1),
      Instant.EPOCH,
      MessageType.none(),
      MessageFlag.none(),
      BufferInfo(
        BufferId(-1),
        NetworkId(-1),
        BufferType.none(),
        -1,
        ""
      ),
      "",
      "",
      "",
      "",
      ""
    ),
    byteBufferOf(
      -1,
      -1,
      -1,
      -1,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      0,
      0,
      -1,
      -1,
      -1,
      -1,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0
    ),
    deserializeFeatureSet = FeatureSet.none(),
    serializeFeatureSet = FeatureSet.none()
  )

  @Test
  fun testNormal() = primitiveSerializerTest(
    MessageSerializer,
    Message(
      MsgId(Int.MAX_VALUE.toLong()),
      Instant.ofEpochMilli(1524601750000),
      MessageType.validValues(),
      MessageFlag.validValues(),
      BufferInfo(
        BufferId.MAX_VALUE,
        NetworkId.MAX_VALUE,
        BufferType.validValues(),
        Int.MAX_VALUE,
        "äẞ\u0000\uFFFF"
      ),
      "äẞ\u0000\uFFFF",
      "",
      "",
      "",
      "äẞ\u0000\uFFFF"
    ),
    byteBufferOf(
      127,
      -1,
      -1,
      -1,
      90,
      -33,
      -109,
      -106,
      0,
      7,
      -1,
      -1,
      -113,
      127,
      -1,
      -1,
      -1,
      127,
      -1,
      -1,
      -1,
      0,
      15,
      127,
      -1,
      -1,
      -1,
      0,
      0,
      0,
      9,
      -61,
      -92,
      -31,
      -70,
      -98,
      0,
      -17,
      -65,
      -65,
      0,
      0,
      0,
      9,
      -61,
      -92,
      -31,
      -70,
      -98,
      0,
      -17,
      -65,
      -65,
      0,
      0,
      0,
      9,
      -61,
      -92,
      -31,
      -70,
      -98,
      0,
      -17,
      -65,
      -65
    ),
    deserializeFeatureSet = FeatureSet.none(),
    serializeFeatureSet = FeatureSet.none()
  )

  @Test
  fun testExtreme() = primitiveSerializerTest(
    MessageSerializer,
    Message(
      MsgId.MAX_VALUE,
      Instant.ofEpochMilli(Int.MAX_VALUE * 10000L),
      MessageType.validValues(),
      MessageFlag.validValues(),
      BufferInfo(
        BufferId.MAX_VALUE,
        NetworkId.MAX_VALUE,
        BufferType.validValues(),
        Int.MAX_VALUE,
        "äẞ\u0000\uFFFF"
      ),
      "äẞ\u0000\uFFFF",
      "äẞ\u0000\uFFFF",
      "äẞ\u0000\uFFFF",
      "äẞ\u0000\uFFFF",
      "äẞ\u0000\uFFFF"
    ),
    byteBufferOf(
      0x7Fu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0xFFu,
      0x00u,
      0x00u,
      0x13u,
      0x87u,
      0xFFu,
      0xFFu,
      0xD8u,
      0xF0u,
      0x00u,
      0x07u,
      0xFFu,
      0xFFu,
      0x8Fu,
      0x7Fu,
      0xFFu,
      0xFFu,
      0xFFu,
      0x7Fu,
      0xFFu,
      0xFFu,
      0xFFu,
      0x00u,
      0x0Fu,
      0x7Fu,
      0xFFu,
      0xFFu,
      0xFFu,
      0x00u,
      0x00u,
      0x00u,
      0x09u,
      0xC3u,
      0xA4u,
      0xE1u,
      0xBAu,
      0x9Eu,
      0x00u,
      0xEFu,
      0xBFu,
      0xBFu,
      0x00u,
      0x00u,
      0x00u,
      0x09u,
      0xC3u,
      0xA4u,
      0xE1u,
      0xBAu,
      0x9Eu,
      0x00u,
      0xEFu,
      0xBFu,
      0xBFu,
      0x00u,
      0x00u,
      0x00u,
      0x09u,
      0xC3u,
      0xA4u,
      0xE1u,
      0xBAu,
      0x9Eu,
      0x00u,
      0xEFu,
      0xBFu,
      0xBFu,
      0x00u,
      0x00u,
      0x00u,
      0x09u,
      0xC3u,
      0xA4u,
      0xE1u,
      0xBAu,
      0x9Eu,
      0x00u,
      0xEFu,
      0xBFu,
      0xBFu,
      0x00u,
      0x00u,
      0x00u,
      0x09u,
      0xC3u,
      0xA4u,
      0xE1u,
      0xBAu,
      0x9Eu,
      0x00u,
      0xEFu,
      0xBFu,
      0xBFu,
      0x00u,
      0x00u,
      0x00u,
      0x09u,
      0xC3u,
      0xA4u,
      0xE1u,
      0xBAu,
      0x9Eu,
      0x00u,
      0xEFu,
      0xBFu,
      0xBFu
    ),
    featureSets = listOf(FeatureSet.all()),
    deserializeFeatureSet = FeatureSet.all(),
    serializeFeatureSet = FeatureSet.all()
  )
}
