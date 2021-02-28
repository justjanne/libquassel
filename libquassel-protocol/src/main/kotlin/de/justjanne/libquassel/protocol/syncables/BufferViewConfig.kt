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
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.flags.BufferActivity
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.state.BufferViewConfigState
import de.justjanne.libquassel.protocol.syncables.stubs.BufferViewConfigStub
import de.justjanne.libquassel.protocol.util.collections.insert
import de.justjanne.libquassel.protocol.util.collections.move
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

open class BufferViewConfig(
  session: Session? = null,
  state: BufferViewConfigState
) : StatefulSyncableObject<BufferViewConfigState>(session, "BufferViewConfig", state),
  BufferViewConfigStub {
  override fun fromVariantMap(properties: QVariantMap) {
    state.update {
      copy(
        buffers = properties["BufferList"].into<QVariantList>()
          ?.mapNotNull { it.into<BufferId>() }
          .orEmpty(),
        removedBuffers = properties["RemovedBuffers"].into<QVariantList>()
          ?.mapNotNull { it.into<BufferId>() }
          ?.toSet()
          .orEmpty(),
        temporarilyRemovedBuffers = properties["TemporarilyRemovedBuffers"].into<QVariantList>()
          ?.mapNotNull { it.into<BufferId>() }
          ?.toSet()
          .orEmpty(),
        bufferViewName = properties["bufferViewName"].into(bufferViewName()),
        networkId = properties["networkId"].into(networkId()),
        addNewBuffersAutomatically = properties["addNewBuffersAutomatically"].into(addNewBuffersAutomatically()),
        sortAlphabetically = properties["sortAlphabetically"].into(sortAlphabetically()),
        hideInactiveBuffers = properties["hideInactiveBuffers"].into(hideInactiveBuffers()),
        hideInactiveNetworks = properties["hideInactiveNetworks"].into(hideInactiveNetworks()),
        disableDecoration = properties["disableDecoration"].into(disableDecoration()),
        allowedBufferTypes = properties["allowedBufferTypes"].into(allowedBufferTypes()),
        minimumActivity = properties["minimumActivity"].into(minimumActivity()),
        showSearch = properties["showSearch"].into(showSearch()),
      )
    }
  }

  override fun toVariantMap() = mapOf(
    "BufferList" to qVariant(
      buffers().map {
        qVariant(it, QuasselType.BufferId)
      },
      QtType.QVariantList
    ),
    "RemovedBuffers" to qVariant(
      removedBuffers().map {
        qVariant(it, QuasselType.BufferId)
      },
      QtType.QVariantList
    ),
    "TemporarilyRemovedBuffers" to qVariant(
      temporarilyRemovedBuffers().map {
        qVariant(it, QuasselType.BufferId)
      },
      QtType.QVariantList
    ),
    "bufferViewName" to qVariant(bufferViewName(), QtType.QString),
    "networkId" to qVariant(networkId(), QuasselType.NetworkId),
    "addNewBuffersAutomatically" to qVariant(addNewBuffersAutomatically(), QtType.Bool),
    "sortAlphabetically" to qVariant(sortAlphabetically(), QtType.Bool),
    "hideInactiveBuffers" to qVariant(hideInactiveBuffers(), QtType.Bool),
    "hideInactiveNetworks" to qVariant(hideInactiveNetworks(), QtType.Bool),
    "disableDecoration" to qVariant(disableDecoration(), QtType.Bool),
    "allowedBufferTypes" to qVariant(allowedBufferTypes().toBits().toInt(), QtType.Int),
    "minimumActivity" to qVariant(minimumActivity().toBits().toInt(), QtType.Int),
    "showSearch" to qVariant(showSearch(), QtType.Bool)
  )

  fun bufferViewId() = state().bufferViewId
  fun bufferViewName() = state().bufferViewName
  fun networkId() = state().networkId
  fun addNewBuffersAutomatically() = state().addNewBuffersAutomatically
  fun sortAlphabetically() = state().sortAlphabetically
  fun hideInactiveBuffers() = state().hideInactiveBuffers
  fun hideInactiveNetworks() = state().hideInactiveNetworks
  fun disableDecoration() = state().disableDecoration
  fun allowedBufferTypes() = state().allowedBufferTypes
  fun minimumActivity() = state().minimumActivity
  fun showSearch() = state().showSearch

  fun buffers() = state().buffers
  fun removedBuffers() = state().removedBuffers
  fun temporarilyRemovedBuffers() = state().temporarilyRemovedBuffers

  override fun addBuffer(buffer: BufferId, pos: Int) {
    state.update {
      copy(
        buffers = buffers.insert(buffer, pos),
        removedBuffers = removedBuffers - buffer,
        temporarilyRemovedBuffers = temporarilyRemovedBuffers - buffer
      )
    }

    super.addBuffer(buffer, pos)
  }

  override fun removeBuffer(buffer: BufferId) {
    state.update {
      copy(
        buffers = buffers - buffer,
        removedBuffers = removedBuffers - buffer,
        temporarilyRemovedBuffers = temporarilyRemovedBuffers + buffer
      )
    }

    super.removeBuffer(buffer)
  }

  override fun removeBufferPermanently(buffer: BufferId) {
    state.update {
      copy(
        buffers = buffers - buffer,
        removedBuffers = removedBuffers + buffer,
        temporarilyRemovedBuffers = temporarilyRemovedBuffers - buffer
      )
    }

    super.removeBufferPermanently(buffer)
  }

  override fun moveBuffer(buffer: BufferId, pos: Int) {
    if (!buffers().contains(buffer)) {
      return
    }

    state.update {
      copy(
        buffers = buffers.move(buffer, pos),
        removedBuffers = removedBuffers - buffer,
        temporarilyRemovedBuffers = temporarilyRemovedBuffers - buffer
      )
    }

    super.moveBuffer(buffer, pos)
  }

  override fun setBufferViewName(value: String) {
    state.update {
      copy(bufferViewName = value)
    }
    super.setBufferViewName(value)
  }

  override fun setAddNewBuffersAutomatically(value: Boolean) {
    state.update {
      copy(addNewBuffersAutomatically = value)
    }
    super.setAddNewBuffersAutomatically(value)
  }

  override fun setAllowedBufferTypes(value: Int) {
    state.update {
      copy(allowedBufferTypes = BufferType.of(value.toUShort()))
    }
    super.setAllowedBufferTypes(value)
  }

  override fun setDisableDecoration(value: Boolean) {
    state.update {
      copy(disableDecoration = value)
    }
    super.setDisableDecoration(value)
  }

  override fun setHideInactiveBuffers(value: Boolean) {
    state.update {
      copy(hideInactiveBuffers = value)
    }
    super.setHideInactiveBuffers(value)
  }

  override fun setHideInactiveNetworks(value: Boolean) {
    state.update {
      copy(hideInactiveNetworks = value)
    }
    super.setHideInactiveNetworks(value)
  }

  override fun setMinimumActivity(value: Int) {
    state.update {
      copy(minimumActivity = BufferActivity.of(value.toUInt()))
    }
    super.setMinimumActivity(value)
  }

  override fun setNetworkId(value: NetworkId) {
    state.update {
      copy(networkId = value)
    }
    super.setNetworkId(value)
  }

  override fun setShowSearch(value: Boolean) {
    state.update {
      copy(showSearch = value)
    }
    super.setShowSearch(value)
  }

  override fun setSortAlphabetically(value: Boolean) {
    state.update {
      copy(sortAlphabetically = value)
    }
    super.setSortAlphabetically(value)
  }

  fun insertBufferSorted(info: BufferInfo) {
    requestAddBuffer(
      info.bufferId,
      buffers()
        .asSequence()
        .withIndex()
        .mapNotNull { (index, value) ->
          IndexedValue(
            index,
            session?.bufferSyncer()?.bufferInfo(value)
              ?: return@mapNotNull null
          )
        }
        .filter { (_, value) -> value.networkId == info.networkId }
        .find { (_, value) ->
          String.CASE_INSENSITIVE_ORDER.compare(value.bufferName, info.bufferName) > 0
        }?.index ?: buffers().size
    )
  }

  fun handleBuffer(info: BufferInfo, unhide: Boolean = false) {
    if (addNewBuffersAutomatically() &&
      !buffers().contains(info.bufferId) &&
      !temporarilyRemovedBuffers().contains(info.bufferId) &&
      !removedBuffers().contains(info.bufferId) &&
      !info.type.contains(BufferType.Status)
    ) {
      insertBufferSorted(info)
    } else if (unhide &&
      !buffers().contains(info.bufferId) &&
      temporarilyRemovedBuffers().contains(info.bufferId)
    ) {
      insertBufferSorted(info)
    }
  }
}
