/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.syncables.common.NetworkConfig
import de.justjanne.libquassel.protocol.syncables.state.NetworkConfigState
import de.justjanne.libquassel.protocol.testutil.nextNetworkConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.random.Random

class NetworkConfigTest {
  @Test
  fun testEmpty() {
    val actual = NetworkConfig().apply {
      update(emptyMap())
    }.state()

    assertEquals(NetworkConfigState(), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextNetworkConfig()

    val actual = NetworkConfig().apply {
      update(NetworkConfig(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class Setters {
    @Test
    fun testAutoWhoDelay() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      val value = random.nextInt()
      assertNotEquals(value, networkConfig.autoWhoDelay())
      networkConfig.setAutoWhoDelay(value)
      assertEquals(value, networkConfig.autoWhoDelay())
    }

    @Test
    fun testAutoWhoEnabled() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      networkConfig.setAutoWhoEnabled(false)
      assertEquals(false, networkConfig.autoWhoEnabled())
      networkConfig.setAutoWhoEnabled(true)
      assertEquals(true, networkConfig.autoWhoEnabled())
    }

    @Test
    fun testAutoWhoInterval() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      val value = random.nextInt()
      assertNotEquals(value, networkConfig.autoWhoInterval())
      networkConfig.setAutoWhoInterval(value)
      assertEquals(value, networkConfig.autoWhoInterval())
    }

    @Test
    fun testAutoWhoNickLimit() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      val value = random.nextInt()
      assertNotEquals(value, networkConfig.autoWhoNickLimit())
      networkConfig.setAutoWhoNickLimit(value)
      assertEquals(value, networkConfig.autoWhoNickLimit())
    }

    @Test
    fun testMaxPingCount() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      val value = random.nextInt()
      assertNotEquals(value, networkConfig.maxPingCount())
      networkConfig.setMaxPingCount(value)
      assertEquals(value, networkConfig.maxPingCount())
    }

    @Test
    fun testPingInterval() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      val value = random.nextInt()
      assertNotEquals(value, networkConfig.pingInterval())
      networkConfig.setPingInterval(value)
      assertEquals(value, networkConfig.pingInterval())
    }

    @Test
    fun testPingTimeoutEnabled() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      networkConfig.setPingTimeoutEnabled(false)
      assertEquals(false, networkConfig.pingTimeoutEnabled())
      networkConfig.setPingTimeoutEnabled(true)
      assertEquals(true, networkConfig.pingTimeoutEnabled())
    }

    @Test
    fun testStandardCtcp() {
      val random = Random(1337)
      val networkConfig = NetworkConfig(state = random.nextNetworkConfig())

      networkConfig.setStandardCtcp(false)
      assertEquals(false, networkConfig.standardCtcp())
      networkConfig.setStandardCtcp(true)
      assertEquals(true, networkConfig.standardCtcp())
    }
  }
}
