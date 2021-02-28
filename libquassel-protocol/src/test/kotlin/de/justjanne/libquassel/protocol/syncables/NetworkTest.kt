/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.state.NetworkState
import de.justjanne.libquassel.protocol.testutil.nextNetwork
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class NetworkTest {
  @Test
  fun testSerialization() {
    val random = Random(1337)
    val networkId = NetworkId(random.nextInt())
    val expected = random.nextNetwork(networkId)

    val actual = Network(state = NetworkState(networkId = networkId)).apply {
      fromVariantMap(Network(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }
}