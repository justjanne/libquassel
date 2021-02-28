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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("BufferViewManager")
interface BufferViewManagerStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addBufferViewConfig(bufferViewConfigId: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "addBufferViewConfig",
      qVariant(bufferViewConfigId, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun newBufferViewConfig(bufferViewConfigId: Int) {
    addBufferViewConfig(bufferViewConfigId)
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestCreateBufferView(properties: QVariantMap) {
    sync(
      target = ProtocolSide.CORE,
      "requestCreateBufferView",
      qVariant(properties, QtType.QVariantMap),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestCreateBufferViews(properties: QVariantList) {
    sync(
      target = ProtocolSide.CORE,
      "requestCreateBufferViews",
      qVariant(properties, QtType.QVariantList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun deleteBufferViewConfig(bufferViewConfigId: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "deleteBufferViewConfig",
      qVariant(bufferViewConfigId, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestDeleteBufferView(bufferViewConfigId: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestDeleteBufferView",
      qVariant(bufferViewConfigId, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
