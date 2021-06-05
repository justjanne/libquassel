/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil.mocks

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.session.SyncProxy
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantList

open class EmptySyncProxy : SyncProxy {
  override fun synchronize(syncable: SyncableStub) = Unit
  override fun stopSynchronize(syncable: SyncableStub) = Unit

  override fun sync(
    target: ProtocolSide,
    className: String,
    objectName: String,
    function: String,
    arguments: QVariantList
  ) = Unit

  override fun rpc(target: ProtocolSide, function: String, arguments: QVariantList) = Unit
}
