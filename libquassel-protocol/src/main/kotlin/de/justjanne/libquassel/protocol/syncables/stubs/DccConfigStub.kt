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
import de.justjanne.libquassel.protocol.models.dcc.DccIpDetectionMode
import de.justjanne.libquassel.protocol.models.dcc.DccPortSelectionMode
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import java.net.InetAddress

@SyncedObject("DccConfig")
interface DccConfigStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setDccEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setDccEnabled",
      qVariant(enabled, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setOutgoingIp(outgoingIp: InetAddress) {
    sync(
      target = ProtocolSide.CLIENT,
      "setOutgoingIp",
      qVariant(outgoingIp, QuasselType.QHostAddress),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setIpDetectionMode(ipDetectionMode: DccIpDetectionMode) {
    sync(
      target = ProtocolSide.CLIENT,
      "setIpDetectionMode",
      qVariant(ipDetectionMode, QuasselType.DccConfigIpDetectionMode),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setPortSelectionMode(portSelectionMode: DccPortSelectionMode) {
    sync(
      target = ProtocolSide.CLIENT,
      "setPortSelectionMode",
      qVariant(portSelectionMode, QuasselType.DccConfigPortSelectionMode),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMinPort(port: UShort) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMinPort",
      qVariant(port, QtType.UShort),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMaxPort(port: UShort) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMaxPort",
      qVariant(port, QtType.UShort),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setChunkSize(chunkSize: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setChunkSize",
      qVariant(chunkSize, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSendTimeout(timeout: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSendTimeout",
      qVariant(timeout, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUsePassiveDcc(use: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUsePassiveDcc",
      qVariant(use, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseFastSend(use: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseFastSend",
      qVariant(use, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
