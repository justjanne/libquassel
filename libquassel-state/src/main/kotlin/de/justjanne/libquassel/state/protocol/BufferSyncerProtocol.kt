/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.state.protocol

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.annotations.SyncedCall
import de.justjanne.libquassel.annotations.SyncedObject
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("BufferSyncer")
interface BufferSyncerProtocol : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun markBufferAsRead(buffer: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "markBufferAsRead",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestMarkBufferAsRead(buffer: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestMarkBufferAsRead",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun mergeBuffersPermanently(buffer: BufferId, buffer2: BufferId) {
    sync(
      target = ProtocolSide.CLIENT,
      "mergeBuffersPermanently",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer2, QuasselType.BufferId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestMergeBuffersPermanently(buffer: BufferId, buffer2: BufferId) {
    sync(
      target = ProtocolSide.CORE,
      "requestMergeBuffersPermanently",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer2, QuasselType.BufferId),
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
  fun renameBuffer(buffer: BufferId, newName: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "renameBuffer",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a String
       */
      qVariant(newName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRenameBuffer(buffer: BufferId, newName: String) {
    sync(
      target = ProtocolSide.CORE,
      "requestRenameBuffer",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a String
       */
      qVariant(newName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMarkerLine(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMarkerLine",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetLastSeenMsg(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetLastSeenMsg",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setLastSeenMsg(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setLastSeenMsg",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetMarkerLine(buffer: BufferId, msgId: MsgId) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetMarkerLine",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(msgId, QuasselType.MsgId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setBufferActivity(buffer: BufferId, count: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setBufferActivity",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(count, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setHighlightCount(buffer: BufferId, count: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setHighlightCount",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(buffer, QuasselType.BufferId),
      /**
       * Construct a QVariant from a Int
       */
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
}
