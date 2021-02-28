/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
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
