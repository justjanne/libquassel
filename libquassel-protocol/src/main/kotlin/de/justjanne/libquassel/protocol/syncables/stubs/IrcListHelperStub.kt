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
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("IrcListHelper")
interface IrcListHelperStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CORE)
  fun requestChannelList(netId: NetworkId, channelFilters: QStringList): QVariantList {
    sync(
      target = ProtocolSide.CORE,
      "requestChannelList",
      qVariant(netId, QuasselType.NetworkId),
      qVariant(channelFilters, QtType.QStringList),
    )
    return emptyList()
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveChannelList(netId: NetworkId, channelFilters: QStringList, channels: QVariantList) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveChannelList",
      qVariant(netId, QuasselType.NetworkId),
      qVariant(channelFilters, QtType.QStringList),
      qVariant(channels, QtType.QVariantList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun reportError(error: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "reportError",
      qVariant(error, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun reportFinishedList(netId: NetworkId) {
    sync(
      target = ProtocolSide.CLIENT,
      "reportFinishedList",
      qVariant(netId, QuasselType.NetworkId),
    )
  }
}
