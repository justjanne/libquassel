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
import de.justjanne.libquassel.protocol.variant.qVariant

interface StatefulSyncableStub : SyncableStub {
  fun fromVariantMap(properties: QVariantMap)
  fun toVariantMap(): QVariantMap

  /**
   * Replaces all properties of the object with the content of the
   * "properties" parameter. This parameter is in network representation.
   */
  fun update(properties: QVariantMap) {
    fromVariantMap(properties)
    sync(
      target = ProtocolSide.CLIENT,
      "update",
      qVariant(properties, QtType.QVariantMap)
    )
  }

  /**
   * Replaces all properties of the object with the content of the
   * "properties" parameter. This parameter is in network representation.
   */
  fun requestUpdate(properties: QVariantMap = toVariantMap()) {
    sync(
      target = ProtocolSide.CORE,
      "requestUpdate",
      qVariant(properties, QtType.QVariantMap)
    )
  }
}
