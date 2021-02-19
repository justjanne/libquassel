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
import de.justjanne.libquassel.protocol.models.TransferIdList
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import java.util.UUID

interface TransferManagerProtocol : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setTransferIds(transferIds: TransferIdList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setTransferIds",
      qVariant(transferIds, QuasselType.TransferIdList)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun onCoreTransferAdded(transferId: UUID) {
    sync(
      target = ProtocolSide.CLIENT,
      "onCoreTransferAdded",
      qVariant(transferId, QtType.Uuid)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
