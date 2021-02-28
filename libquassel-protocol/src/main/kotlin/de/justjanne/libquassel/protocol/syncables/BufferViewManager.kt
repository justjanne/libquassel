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

import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.BufferViewConfigState
import de.justjanne.libquassel.protocol.syncables.state.BufferViewManagerState
import de.justjanne.libquassel.protocol.syncables.stubs.BufferViewManagerStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QVariant_
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

open class BufferViewManager(
  session: Session? = null,
  state: BufferViewManagerState = BufferViewManagerState()
) : StatefulSyncableObject<BufferViewManagerState>(session, "BufferViewManager", state),
  BufferViewManagerStub {
  override fun fromVariantMap(properties: QVariantMap) {
    properties["BufferViewIds"].into<QVariantList>()
      ?.mapNotNull<QVariant_, Int>(QVariant_::into)
      ?.forEach(this::addBufferViewConfig)
  }

  override fun toVariantMap() = mapOf(
    "BufferViewIds" to qVariant(
      state().bufferViewConfigs.map {
        qVariant(it.key, QtType.Int)
      },
      QtType.QVariantList
    )
  )

  fun contains(bufferViewId: Int) = state().contains(bufferViewId)
  fun bufferViewConfig(bufferViewId: Int) = state().bufferViewConfig(bufferViewId)
  fun bufferViewConfigs() = state().bufferViewConfigs()

  override fun addBufferViewConfig(bufferViewConfigId: Int) {
    if (contains(bufferViewConfigId)) {
      return
    }

    val config = BufferViewConfig(
      session,
      BufferViewConfigState(
        bufferViewId = bufferViewConfigId
      )
    )
    session?.synchronize(config)
    state.update {
      copy(bufferViewConfigs = bufferViewConfigs + Pair(bufferViewConfigId, config))
    }

    super.addBufferViewConfig(bufferViewConfigId)
  }

  fun handleBuffer(info: BufferInfo, unhide: Boolean = false) {
    for (bufferViewConfig in bufferViewConfigs()) {
      bufferViewConfig.handleBuffer(info, unhide)
    }
  }
}
