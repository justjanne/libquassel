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

import de.justjanne.libquassel.protocol.features.FeatureSet
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.NoSerializerForTypeException
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.variant.QVariant_
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Tag("QtSerializerTest")
class QVariantSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      QVariantSerializer,
      QtType.QVariant.serializer<QVariant_>(),
    )
  }

  @Test
  fun testUnregisteredQtType() {
    assertThrows<NoSerializerForTypeException> {
      QVariantSerializer.deserialize(
        byteBufferOf(0x00u, 0x00u, 0x01u, 0x00u, 0x00u),
        FeatureSet.all()
      )
    }
  }

  @Test
  fun testUnknownQtType() {
    assertThrows<NoSerializerForTypeException> {
      QVariantSerializer.deserialize(
        byteBufferOf(0x00u, 0xFFu, 0x00u, 0x00u, 0x00u),
        FeatureSet.all()
      )
    }
  }

  @Test
  fun testUnregisteredQuasselType() {
    assertThrows<NoSerializerForTypeException> {
      QVariantSerializer.deserialize(
        byteBufferOf(
          // QtType
          0x00u, 0x00u, 0x00u, 0x7Fu,
          // isNull
          0x00u,
          // QuasselType length
          0x00u, 0x00u, 0x00u, 0x00u,
        ),
        FeatureSet.all()
      )
    }
  }

  @Test
  fun testUnknownQuasselType() {
    assertThrows<NoSerializerForTypeException> {
      QVariantSerializer.deserialize(
        byteBufferOf(
          // QtType
          0x00u, 0x00u, 0x00u, 0x7Fu,
          // isNull
          0x00u,
          // QuasselType length
          0x00u, 0x00u, 0x00u, 0x03u,
          // "foo"
          0x66u, 0x6fu, 0x6fu
        ),
        FeatureSet.all()
      )
    }
  }
}
