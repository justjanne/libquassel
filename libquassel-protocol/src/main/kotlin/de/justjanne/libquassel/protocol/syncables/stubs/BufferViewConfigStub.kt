/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.stubs

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.annotations.SyncedCall
import de.justjanne.libquassel.annotations.SyncedObject
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("BufferViewConfig")
interface BufferViewConfigStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addBuffer(buffer: BufferId, pos: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "addBuffer",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestAddBuffer(buffer: BufferId, pos: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestAddBuffer",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun moveBuffer(buffer: BufferId, pos: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "moveBuffer",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestMoveBuffer(buffer: BufferId, pos: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestMoveBuffer",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeBuffer(buffer: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeBuffer",
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveBuffer(buffer: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveBuffer",
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeBufferPermanently(buffer: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeBufferPermanently",
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveBufferPermanently(buffer: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveBufferPermanently",
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setBufferViewName(value: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setBufferViewName",
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetBufferViewName(value: String) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetBufferViewName",
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAddNewBuffersAutomatically(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAddNewBuffersAutomatically",
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAllowedBufferTypes(value: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAllowedBufferTypes",
      qVariant(value, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setDisableDecoration(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setDisableDecoration",
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHideInactiveBuffers(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHideInactiveBuffers",
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHideInactiveNetworks(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHideInactiveNetworks",
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMinimumActivity(value: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMinimumActivity",
      qVariant(value, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNetworkId(value: NetworkId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNetworkId",
      qVariant(value, QuasselType.NetworkId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setShowSearch(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setShowSearch",
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSortAlphabetically(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSortAlphabetically",
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
