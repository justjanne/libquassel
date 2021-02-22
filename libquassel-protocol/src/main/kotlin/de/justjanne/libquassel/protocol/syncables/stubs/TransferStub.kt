/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.stubs

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.annotations.SyncedCall
import de.justjanne.libquassel.annotations.SyncedObject
import de.justjanne.libquassel.protocol.models.TransferDirection
import de.justjanne.libquassel.protocol.models.TransferStatus
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import java.net.InetAddress
import java.nio.ByteBuffer

@SyncedObject("Transfer")
interface TransferStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun accept(savePath: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "accept",
      /**
       * Construct a QVariant from a String
       */
      qVariant(savePath, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestAccepted(peer: ULong = 0uL) {
    sync(
      target = ProtocolSide.CORE,
      "requestAccepted",
      /**
       * Construct a QVariant from a ULong
       */
      qVariant(peer, QtType.ULong)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun reject() {
    sync(
      target = ProtocolSide.CLIENT,
      "reject"
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestRejected(peer: ULong = 0uL) {
    sync(
      target = ProtocolSide.CORE,
      "requestRejected",
      /**
       * Construct a QVariant from a ULong
       */
      qVariant(peer, QtType.ULong)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setStatus(status: TransferStatus) {
    sync(
      target = ProtocolSide.CLIENT,
      "setStatus",
      qVariant(status, QuasselType.TransferStatus)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setDirection(direction: TransferDirection) {
    sync(
      target = ProtocolSide.CLIENT,
      "setDirection",
      qVariant(direction, QuasselType.TransferDirection)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAddress(address: InetAddress) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAddress",
      /**
       * Construct a QVariant from a InetAddress
       */
      qVariant(address, QuasselType.QHostAddress)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setPort(port: UShort) {
    sync(
      target = ProtocolSide.CLIENT,
      "setPort",
      /**
       * Construct a QVariant from a UShort
       */
      qVariant(port, QtType.UShort)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setFileName(fileName: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setFileName",
      /**
       * Construct a QVariant from a String
       */
      qVariant(fileName, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setFileSize(fileSize: ULong) {
    sync(
      target = ProtocolSide.CLIENT,
      "setFileSize",
      /**
       * Construct a QVariant from a ULong
       */
      qVariant(fileSize, QtType.ULong)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNick(nick: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNick",
      /**
       * Construct a QVariant from a String
       */
      qVariant(nick, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setError(errorString: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setError",
      /**
       * Construct a QVariant from a String
       */
      qVariant(errorString, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun dataReceived(peer: ULong, data: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "dataReceived",
      qVariant(peer, QuasselType.PeerPtr),
      /**
       * Construct a QVariant from a ByteBuffer
       */
      qVariant(data, QtType.QByteArray)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun cleanUp() {
    sync(
      target = ProtocolSide.CLIENT,
      "cleanUp",
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}