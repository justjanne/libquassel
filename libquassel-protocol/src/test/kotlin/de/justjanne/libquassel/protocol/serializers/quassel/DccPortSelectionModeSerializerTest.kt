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
package de.justjanne.libquassel.protocol.serializers.quassel

import de.justjanne.libquassel.protocol.models.DccPortSelectionMode
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import de.justjanne.libquassel.protocol.testutil.quasselSerializerTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DccPortSelectionModeSerializerTest {
  @Test
  fun testIsRegistered() {
    assertEquals(
      DccPortSelectionModeSerializer,
      QuasselType.DccConfigPortSelectionMode.serializer<DccPortSelectionMode>(),
    )
  }

  @Test
  fun testAutomatic() = quasselSerializerTest(
    DccPortSelectionModeSerializer,
    DccPortSelectionMode.Automatic,
    byteBufferOf(0x00u)
  )

  @Test
  fun testManual() = quasselSerializerTest(
    DccPortSelectionModeSerializer,
    DccPortSelectionMode.Manual,
    byteBufferOf(0x01u)
  )

  @Test
  fun testNull() = quasselSerializerTest(
    DccPortSelectionModeSerializer,
    null,
    byteBufferOf(0x00u),
    deserializeFeatureSet = null,
    featureSets = emptyList(),
  )
}
