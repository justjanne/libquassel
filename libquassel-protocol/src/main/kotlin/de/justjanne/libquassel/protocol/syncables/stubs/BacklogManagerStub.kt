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
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("BacklogManager")
interface BacklogManagerStub : SyncableStub {
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
      qVariant(bufferId, QuasselType.BufferId),
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
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
      qVariant(bufferId, QuasselType.BufferId),
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
      qVariant(additional, QtType.Int),
      qVariant(type, QtType.Int),
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
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
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
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
      qVariant(additional, QtType.Int),
      qVariant(type, QtType.Int),
      qVariant(flags, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveBacklog(
    bufferId: BufferId,
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    messages: QVariantList
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklog",
      qVariant(bufferId, QuasselType.BufferId),
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
      qVariant(additional, QtType.Int),
      qVariant(messages, QtType.QVariantList),
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
    flags: Int = -1,
    messages: QVariantList
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklogFiltered",
      qVariant(bufferId, QuasselType.BufferId),
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
      qVariant(additional, QtType.Int),
      qVariant(type, QtType.Int),
      qVariant(flags, QtType.Int),
      qVariant(messages, QtType.QVariantList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveBacklogAll(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    messages: QVariantList
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklogAll",
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
      qVariant(additional, QtType.Int),
      qVariant(messages, QtType.QVariantList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveBacklogAllFiltered(
    first: MsgId = MsgId(-1),
    last: MsgId = MsgId(-1),
    limit: Int = -1,
    additional: Int = 0,
    type: Int = -1,
    flags: Int = -1,
    messages: QVariantList
  ) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveBacklogAllFiltered",
      qVariant(first, QuasselType.MsgId),
      qVariant(last, QuasselType.MsgId),
      qVariant(limit, QtType.Int),
      qVariant(additional, QtType.Int),
      qVariant(type, QtType.Int),
      qVariant(flags, QtType.Int),
      qVariant(messages, QtType.QVariantList),
    )
  }
}
