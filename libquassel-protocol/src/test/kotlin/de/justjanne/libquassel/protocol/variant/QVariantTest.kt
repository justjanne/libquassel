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

package de.justjanne.libquassel.protocol.variant

import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.testutil.byteBufferOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QVariantTest {
  @Test
  fun testString() {
    assertEquals(
      "QVariant(QByteArray, DEADBEEF)",
      qVariant(
        byteBufferOf(0xDEu, 0xADu, 0xBEu, 0xEFu),
        QtType.QByteArray
      ).toString()
    )
    assertEquals(
      "QVariant(QString, DEADBEEF)",
      qVariant(
        "DEADBEEF",
        QtType.QString
      ).toString()
    )
    assertEquals(
      "QVariant(BufferId, BufferId(-1))",
      qVariant(
        BufferId(-1),
        QuasselType.BufferId
      ).toString()
    )
  }
}
