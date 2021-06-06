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
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("BufferSyncer")
interface BufferSyncerStub : StatefulSyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun markBufferAsRead(buffer: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "markBufferAsRead",
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestMarkBufferAsRead(buffer: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestMarkBufferAsRead",
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun mergeBuffersPermanently(buffer: BufferId, buffer2: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "mergeBuffersPermanently",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(buffer2, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestMergeBuffersPermanently(buffer: BufferId, buffer2: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestMergeBuffersPermanently",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(buffer2, QuasselType.BufferId),
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
  fun renameBuffer(buffer: BufferId, newName: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "renameBuffer",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(newName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRenameBuffer(buffer: BufferId, newName: String) {
    sync(
      target = ProtocolSide.CORE,
      "requestRenameBuffer",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(newName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMarkerLine(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMarkerLine",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetLastSeenMsg(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetLastSeenMsg",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setLastSeenMsg(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setLastSeenMsg",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetMarkerLine(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetMarkerLine",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setBufferActivity(buffer: BufferId, types: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setBufferActivity",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(types, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHighlightCount(buffer: BufferId, count: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHighlightCount",
      qVariant(buffer, QuasselType.BufferId),
      qVariant(count, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun requestPurgeBufferIds() {
    sync(
      target = ProtocolSide.CLIENT,
      "requestPurgeBufferIds"
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
