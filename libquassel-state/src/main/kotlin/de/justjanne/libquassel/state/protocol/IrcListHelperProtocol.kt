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
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("IrcListHelper")
interface IrcListHelperProtocol : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CORE)
  fun requestChannelList(netId: NetworkId, channelFilters: QStringList): QVariantList {
    sync(
      target = ProtocolSide.CORE,
      "requestChannelList",
      /**
       * Construct a QVariant from a Message
       */
      qVariant(netId, QuasselType.NetworkId),
      /**
       * Construct a QVariant from a QStringList
       */
      qVariant(channelFilters, QtType.QStringList),
    )
    return emptyList()
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun receiveChannelList(netId: NetworkId, channelFilters: QStringList, channels: QVariantList) {
    sync(
      target = ProtocolSide.CLIENT,
      "receiveChannelList",
      /**
       * Construct a QVariant from a Message
       */
      qVariant(netId, QuasselType.NetworkId),
      /**
       * Construct a QVariant from a QStringList
       */
      qVariant(channelFilters, QtType.QStringList),
      /**
       * Construct a QVariant from a QVariantList
       */
      qVariant(channels, QtType.QVariantList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun reportError(error: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "reportError",
      /**
       * Construct a QVariant from a String?
       */
      qVariant(error, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun reportFinishedList(netId: NetworkId) {
    sync(
      target = ProtocolSide.CLIENT,
      "reportFinishedList",
      /**
       * Construct a QVariant from a Message
       */
      qVariant(netId, QuasselType.NetworkId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
