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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

@SyncedObject("NetworkConfig")
interface NetworkConfigStub : StatefulSyncableStub {
  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoDelay(delay: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoDelay",
      qVariant(delay, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoWhoDelay(delay: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoWhoDelay",
      qVariant(delay, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoEnabled",
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoWhoEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoWhoEnabled",
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoInterval(interval: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoInterval",
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoWhoInterval(interval: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoWhoInterval",
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoNickLimit(limit: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoNickLimit",
      qVariant(limit, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setAutoWhoNickLimit(limit: Int) {
    sync(
      target = ProtocolSide.CORE,
      "setAutoWhoNickLimit",
      qVariant(limit, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetMaxPingCount(count: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetMaxPingCount",
      qVariant(count, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setMaxPingCount(count: Int) {
    sync(
      target = ProtocolSide.CORE,
      "setMaxPingCount",
      qVariant(count, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetPingInterval(interval: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetPingInterval",
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setPingInterval(interval: Int) {
    sync(
      target = ProtocolSide.CORE,
      "setPingInterval",
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetPingTimeoutEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetPingTimeoutEnabled",
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setPingTimeoutEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "setPingTimeoutEnabled",
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetStandardCtcp(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetStandardCtcp",
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setStandardCtcp(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "setStandardCtcp",
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
