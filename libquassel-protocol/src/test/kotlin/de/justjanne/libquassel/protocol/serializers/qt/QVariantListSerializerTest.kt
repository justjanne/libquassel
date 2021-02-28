/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.qt

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.qVariant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QtSerializerTest")
class QVariantListSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QVariantListSerializer,
      QtType.QVariantList.serializer<QVariantList>(),
    )
  }

  @Test
  fun testEmpty() = primitiveSerializerTest(
    QVariantListSerializer,
    listOf(),
    byteBufferOf(0, 0, 0, 0)
  )

  @Test
  fun testNormal() = primitiveSerializerTest(
    QVariantListSerializer,
    listOf(
      qVariant("AzureDiamond", QtType.QString),
      qVariant("hunter2", QtType.QString)
    ),
    byteBufferOf(
      0x00u,
      0x00u,
      0x00u,
      0x02u,
      0x00u,
      0x00u,
      0x00u,
      0x0Au,
      0x00u,
      0x00u,
      0x00u,
      0x00u,
      0x18u,
      0x00u,
      0x41u,
      0x00u,
      0x7Au,
      0x00u,
      0x75u,
      0x00u,
      0x72u,
      0x00u,
      0x65u,
      0x00u,
      0x44u,
      0x00u,
      0x69u,
      0x00u,
      0x61u,
      0x00u,
      0x6Du,
      0x00u,
      0x6Fu,
      0x00u,
      0x6Eu,
      0x00u,
      0x64u,
      0x00u,
      0x00u,
      0x00u,
      0x0Au,
      0x00u,
      0x00u,
      0x00u,
      0x00u,
      0x0Eu,
      0x00u,
      0x68u,
      0x00u,
      0x75u,
      0x00u,
      0x6Eu,
      0x00u,
      0x74u,
      0x00u,
      0x65u,
      0x00u,
      0x72u,
      0x00u,
      0x32u
    )
  )
}
