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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

@SyncedObject("CertManager")
interface CertManagerStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSslCert(encoded: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSslCert",
      /**
       * Construct a QVariant from a ByteBuffer
       */
      qVariant(encoded, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSslKey(encoded: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSslKey",
      /**
       * Construct a QVariant from a ByteBuffer
       */
      qVariant(encoded, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
