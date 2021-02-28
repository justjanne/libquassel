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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QVariant_
import de.justjanne.libquassel.protocol.variant.qVariant

interface SyncableStub {
  val className: String
  val objectName: String
  val initialized: Boolean
  val session: Session?

  fun fromVariantMap(properties: QVariantMap)
  fun toVariantMap(): QVariantMap

  fun sync(target: ProtocolSide, function: String, vararg arg: QVariant_) {
    if (initialized) {
      session?.sync(target, className, objectName, function, arg.toList())
    }
  }

  fun rpc(target: ProtocolSide, function: String, vararg arg: QVariant_) {
    if (initialized) {
      session?.rpc(target, function, arg.toList())
    }
  }

  fun update(properties: QVariantMap) {
    fromVariantMap(properties)
    sync(
      target = ProtocolSide.CLIENT,
      "update",
      qVariant(properties, QtType.QVariantMap)
    )
  }

  fun requestUpdate(properties: QVariantMap = toVariantMap()) {
    sync(
      target = ProtocolSide.CORE,
      "requestUpdate",
      qVariant(properties, QtType.QVariantMap)
    )
  }
}
