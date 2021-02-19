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
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant

interface NetworkConfigProtocol : SyncableProtocol {
  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoDelay(delay: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoDelay",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(delay, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoWhoDelay(delay: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoWhoDelay",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(delay, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoEnabled",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoWhoEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoWhoEnabled",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoInterval(interval: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoInterval",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoWhoInterval(interval: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoWhoInterval",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetAutoWhoNickLimit(limit: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetAutoWhoNickLimit",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setAutoWhoNickLimit(limit: Int) {
    sync(
      target = ProtocolSide.CORE,
      "setAutoWhoNickLimit",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(limit, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetMaxPingCount(count: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetMaxPingCount",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(count, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setMaxPingCount(count: Int) {
    sync(
      target = ProtocolSide.CORE,
      "setMaxPingCount",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(count, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetPingInterval(interval: Int) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetPingInterval",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setPingInterval(interval: Int) {
    sync(
      target = ProtocolSide.CORE,
      "setPingInterval",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(interval, QtType.Int)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetPingTimeoutEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetPingTimeoutEnabled",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setPingTimeoutEnabled(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "setPingTimeoutEnabled",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetStandardCtcp(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetStandardCtcp",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun setStandardCtcp(enabled: Boolean) {
    sync(
      target = ProtocolSide.CORE,
      "setStandardCtcp",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(enabled, QtType.Bool)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
