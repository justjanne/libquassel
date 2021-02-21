/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
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
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestAddBuffer(buffer: BufferId, pos: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestAddBuffer",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun moveBuffer(buffer: BufferId, pos: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "moveBuffer",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestMoveBuffer(buffer: BufferId, pos: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestMoveBuffer",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(pos, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeBuffer(buffer: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeBuffer",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveBuffer(buffer: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveBuffer",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeBufferPermanently(buffer: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeBufferPermanently",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRemoveBufferPermanently(buffer: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestRemoveBufferPermanently",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setBufferViewName(value: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setBufferViewName",
      /**
       * Construct a QVariant from a String
       */
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetBufferViewName(value: String) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetBufferViewName",
      /**
       * Construct a QVariant from a String
       */
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAddNewBuffersAutomatically(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAddNewBuffersAutomatically",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAllowedBufferTypes(value: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAllowedBufferTypes",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(value, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setDisableDecoration(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setDisableDecoration",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHideInactiveBuffers(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHideInactiveBuffers",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHideInactiveNetworks(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHideInactiveNetworks",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMinimumActivity(value: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMinimumActivity",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(value, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNetworkId(value: NetworkId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNetworkId",
      /**
       * Construct a QVariant from a Message
       */
      qVariant(value, QuasselType.NetworkId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setShowSearch(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setShowSearch",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSortAlphabetically(value: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSortAlphabetically",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(value, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
