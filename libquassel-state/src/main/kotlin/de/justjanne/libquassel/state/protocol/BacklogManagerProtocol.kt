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

@SyncedObject("BacklogManager")
interface BacklogManagerProtocol : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CORE)
  fun requestBacklog(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ) {
    sync(
      target = ProtocolSide.CORE,
      "requestBacklog",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(bufferId, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestBacklogFiltered(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: Int = -1,
    flags: Int = -1
  ) {
    sync(
      target = ProtocolSide.CORE,
      "requestBacklogFiltered",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(bufferId, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(type, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(flags, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestBacklogAll(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ) {
    sync(
      target = ProtocolSide.CORE,
      "requestBacklogAll",
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestBacklogAllFiltered(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: Int = -1,
    flags: Int = -1
  ) {
    sync(
      target = ProtocolSide.CORE,
      "requestBacklogAll",
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(type, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(flags, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveBacklog(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklog",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(bufferId, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveBacklogFiltered(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: Int = -1,
    flags: Int = -1
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklogFiltered",
      /**
       * Construct a QVariant from a BufferId
       */
      qVariant(bufferId, QuasselType.BufferId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(type, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(flags, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveBacklogAll(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklogAll",
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveBacklogAllFiltered(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: Int = -1,
    flags: Int = -1
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklogAllFiltered",
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(first, QuasselType.MsgId),
      /**
       * Construct a QVariant from a MsgId
       */
      qVariant(last, QuasselType.MsgId),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(additional, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(type, QtType.Int),
      /**
       * Construct a QVariant from a Int
       */
      qVariant(flags, QtType.Int),
    )
  }
}
