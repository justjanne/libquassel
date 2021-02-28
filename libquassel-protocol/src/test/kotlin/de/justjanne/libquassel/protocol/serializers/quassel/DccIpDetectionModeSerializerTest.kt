/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.models.dcc.DccIpDetectionMode
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.primitiveSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("QuasselSerializerTest")
class DccIpDetectionModeSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      DccIpDetectionModeSerializer,
      QuasselType.DccConfigIpDetectionMode.serializer<DccIpDetectionMode>(),
    )
  }

  @Test
  fun testAutomatic() = primitiveSerializerTest(
    DccIpDetectionModeSerializer,
    DccIpDetectionMode.Automatic,
    byteBufferOf(0x00u)
  )

  @Test
  fun testManual() = primitiveSerializerTest(
    DccIpDetectionModeSerializer,
    DccIpDetectionMode.Manual,
    byteBufferOf(0x01u)
  )

  @Test
  fun testNull() = primitiveSerializerTest(
    DccIpDetectionModeSerializer,
    null,
    byteBufferOf(0x00u),
    deserializeFeatureSet = null,
    featureSets = emptyList(),
  )
}
