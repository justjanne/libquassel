/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.models.BufferActivity
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.state.BufferViewConfigState
import de.justjanne.libquassel.protocol.testutil.nextBufferViewConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.random.Random

class BufferViewConfigTest {
  @Test
  fun testEmpty() {
    val state = BufferViewConfigState(bufferViewId = 1)
    val actual = BufferViewConfig(state = state).apply {
      update(emptyMap())
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextBufferViewConfig(bufferViewId = 1)

    val actual = BufferViewConfig(
      state = BufferViewConfigState(
        bufferViewId = expected.bufferViewId,
      )
    ).apply {
      update(BufferViewConfig(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class AddBuffer {
    @Test
    fun new() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = BufferId(105)
      value.addBuffer(buffer, 3)
      assertEquals(3, value.buffers().indexOf(buffer))
      assertEquals(bufferSize + 1, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun existing() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.buffers().first()
      value.addBuffer(buffer, 3)
      assertEquals(0, value.buffers().indexOf(buffer))
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun hidden() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.hiddenBuffers().first()
      value.addBuffer(buffer, 3)
      assertEquals(3, value.buffers().indexOf(buffer))
      assertEquals(bufferSize + 1, value.buffers().size)
      assertEquals(hiddenSize - 1, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun removed() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.removedBuffers().first()
      value.addBuffer(buffer, 3)
      assertEquals(3, value.buffers().indexOf(buffer))
      assertEquals(bufferSize + 1, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize - 1, value.removedBuffers().size)
    }
  }

  @Nested
  inner class HideBuffer {
    @Test
    fun new() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = BufferId(105)
      value.hideBuffer(buffer)
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize + 1, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun existing() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.buffers().first()
      value.hideBuffer(buffer)
      assertEquals(bufferSize - 1, value.buffers().size)
      assertEquals(hiddenSize + 1, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun hidden() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.hiddenBuffers().first()
      value.hideBuffer(buffer)
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun removed() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.removedBuffers().first()
      value.hideBuffer(buffer)
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize + 1, value.hiddenBuffers().size)
      assertEquals(removedSize - 1, value.removedBuffers().size)
    }
  }

  @Nested
  inner class RemoveBuffer {
    @Test
    fun new() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = BufferId(105)
      value.removeBuffer(buffer)
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize + 1, value.removedBuffers().size)
    }

    @Test
    fun existing() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.buffers().first()
      value.removeBuffer(buffer)
      assertEquals(bufferSize - 1, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize + 1, value.removedBuffers().size)
    }

    @Test
    fun hidden() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.hiddenBuffers().first()
      value.removeBuffer(buffer)
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize - 1, value.hiddenBuffers().size)
      assertEquals(removedSize + 1, value.removedBuffers().size)
    }

    @Test
    fun removed() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.removedBuffers().first()
      value.removeBuffer(buffer)
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }
  }

  @Nested
  inner class MoveBuffer {
    @Test
    fun new() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = BufferId(105)
      value.moveBuffer(buffer, 3)
      assertEquals(-1, value.buffers().indexOf(buffer))
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun existing() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.buffers().first()
      println(value.buffers())
      value.moveBuffer(buffer, 3)
      println(value.buffers())
      assertEquals(3, value.buffers().indexOf(buffer))
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun hidden() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.hiddenBuffers().first()
      value.moveBuffer(buffer, 3)
      assertEquals(-1, value.buffers().indexOf(buffer))
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }

    @Test
    fun removed() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val bufferSize = value.buffers().size
      val hiddenSize = value.hiddenBuffers().size
      val removedSize = value.removedBuffers().size
      val buffer = value.removedBuffers().first()
      value.moveBuffer(buffer, 3)
      assertEquals(-1, value.buffers().indexOf(buffer))
      assertEquals(bufferSize, value.buffers().size)
      assertEquals(hiddenSize, value.hiddenBuffers().size)
      assertEquals(removedSize, value.removedBuffers().size)
    }
  }

  @Nested
  inner class Setters {
    @Test
    fun testBufferViewName() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val data = "All Chats"
      assertNotEquals(data, value.bufferViewName())
      value.setBufferViewName(data)
      assertEquals(data, value.bufferViewName())
    }

    @Test
    fun testAddNewBuffersAutomatically() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      value.setAddNewBuffersAutomatically(false)
      assertEquals(false, value.addNewBuffersAutomatically())
      value.setAddNewBuffersAutomatically(true)
      assertEquals(true, value.addNewBuffersAutomatically())
    }

    @Test
    fun testAllowedBufferTypes() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val data = BufferType.of(
        BufferType.Channel,
        BufferType.Status,
        BufferType.Query
      )
      assertNotEquals(data, value.allowedBufferTypes())
      value.setAllowedBufferTypes(data.toBits().toInt())
      assertEquals(data, value.allowedBufferTypes())
    }

    @Test
    fun testDisableDecoration() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      value.setDisableDecoration(false)
      assertEquals(false, value.disableDecoration())
      value.setDisableDecoration(true)
      assertEquals(true, value.disableDecoration())
    }

    @Test
    fun testHideInactiveBuffers() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      value.setHideInactiveBuffers(false)
      assertEquals(false, value.hideInactiveBuffers())
      value.setHideInactiveBuffers(true)
      assertEquals(true, value.hideInactiveBuffers())
    }

    @Test
    fun testHideInactiveNetworks() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      value.setHideInactiveNetworks(false)
      assertEquals(false, value.hideInactiveNetworks())
      value.setHideInactiveNetworks(true)
      assertEquals(true, value.hideInactiveNetworks())
    }

    @Test
    fun testMinimumActivity() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val data = BufferActivity.Highlight
      assertNotEquals(data, value.minimumActivity())
      value.setMinimumActivity(data.value)
      assertEquals(data, value.minimumActivity())
    }

    @Test
    fun testNetworkId() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      val data = NetworkId(random.nextInt())
      assertNotEquals(data, value.networkId())
      value.setNetworkId(data)
      assertEquals(data, value.networkId())
    }

    @Test
    fun testShowSearch() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      value.setShowSearch(false)
      assertEquals(false, value.showSearch())
      value.setShowSearch(true)
      assertEquals(true, value.showSearch())
    }

    @Test
    fun testSortAlphabetically() {
      val random = Random(1337)
      val value = BufferViewConfig(
        state = random.nextBufferViewConfig(bufferViewId = 1)
      )

      value.setSortAlphabetically(false)
      assertEquals(false, value.sortAlphabetically())
      value.setSortAlphabetically(true)
      assertEquals(true, value.sortAlphabetically())
    }
  }
}
