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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("AliasManager")
interface AliasManager : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CORE)
  fun addAlias(name: String, expansion: String) {
    sync(
      target = ProtocolSide.CORE,
      "addAlias",
      /**
       * Construct a QVariant from a String
       */
      qVariant(name, QtType.QString),
      /**
       * Construct a QVariant from a String
       */
      qVariant(expansion, QtType.QString)
    )
  }
}
