/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.variant.QVariant_

interface SyncableStub {
  val className: String
  val objectName: String
  var initialized: Boolean
  val session: Session?

  fun sync(target: ProtocolSide, function: String, vararg arg: QVariant_) {
    if (initialized) {
      session?.proxy?.sync(target, className, objectName, function, arg.toList())
    }
  }

  fun rpc(target: ProtocolSide, function: String, vararg arg: QVariant_) {
    if (initialized) {
      session?.proxy?.rpc(target, function, arg.toList())
    }
  }
}
