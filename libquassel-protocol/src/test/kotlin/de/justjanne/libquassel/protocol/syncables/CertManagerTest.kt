/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.syncables.common.CertManager
import de.justjanne.libquassel.protocol.syncables.state.CertManagerState
import de.justjanne.libquassel.protocol.testutil.nextCertManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class CertManagerTest {
  @Test
  fun testEmpty() {
    val identityId = IdentityId(0)
    val actual = CertManager(state = CertManagerState(identityId)).apply {
      update(emptyMap())
    }.state()

    assertEquals(CertManagerState(identityId), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextCertManager()

    val actual = CertManager(state = CertManagerState(identityId = expected.identityId)).apply {
      update(CertManager(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}
