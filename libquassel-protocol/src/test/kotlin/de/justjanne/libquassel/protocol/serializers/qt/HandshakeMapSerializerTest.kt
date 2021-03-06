/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.matchers.MapMatcher
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import de.justjanne.libquassel.protocol.variant.qVariant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QtSerializerTest")
class HandshakeMapSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QVariantMapSerializer.javaType,
      HandshakeMapSerializer.javaType,
    )
  }

  @Test
  fun testEmpty() = primitiveSerializerTest(
    HandshakeMapSerializer,
    mapOf(),
    byteBufferOf(0, 0, 0, 0)
  )

  @Test
  fun testNormal() = primitiveSerializerTest(
    HandshakeMapSerializer,
    mapOf(
      "Username" to qVariant("AzureDiamond", QtType.QString),
      "Password" to qVariant("hunter2", QtType.QString)
    ),
    byteBufferOf(
      0x00, 0x00, 0x00, 0x04,
      0x00, 0x00, 0x00, 0x0C,
      0x00,
      0x00, 0x00, 0x00, 0x08,
      0x55, 0x73, 0x65, 0x72, 0x6E, 0x61, 0x6D, 0x65,
      0x00, 0x00, 0x00, 0x0A,
      0x00,
      0x00, 0x00, 0x00, 0x18,
      0x00, 0x41, 0x00, 0x7A, 0x00, 0x75, 0x00, 0x72, 0x00, 0x65, 0x00, 0x44,
      0x00, 0x69, 0x00, 0x61, 0x00, 0x6D, 0x00, 0x6F, 0x00, 0x6E, 0x00, 0x64,
      0x00, 0x00, 0x00, 0x0C,
      0x00,
      0x00, 0x00, 0x00, 0x08,
      0x50, 0x61, 0x73, 0x73, 0x77, 0x6F, 0x72, 0x64,
      0x00, 0x00, 0x00, 0x0A,
      0x00,
      0x00, 0x00, 0x00, 0x0E,
      0x00, 0x68, 0x00, 0x75, 0x00, 0x6E, 0x00, 0x74, 0x00, 0x65, 0x00, 0x72,
      0x00, 0x32
    ),
    ::MapMatcher
  )

  @Test
  fun testNullKey() = primitiveSerializerTest(
    HandshakeMapSerializer,
    mapOf(
      "" to qVariant<String?>(null, QtType.QString)
    ),
    byteBufferOf(
      // length
      0x00u, 0x00u, 0x00u, 0x02u,
      // type of value
      0x00u, 0x00u, 0x00u, 0x0Au,
      // isNull of value
      0x00u,
      // length of key
      0xFFu, 0xFFu, 0xFFu, 0xFFu,
      // type of value
      0x00u, 0x00u, 0x00u, 0x0Au,
      // isNull of value
      0x00u,
      // length of value
      0xFFu, 0xFFu, 0xFFu, 0xFFu
    ),
    ::MapMatcher,
    serializeFeatureSet = null
  )
}
