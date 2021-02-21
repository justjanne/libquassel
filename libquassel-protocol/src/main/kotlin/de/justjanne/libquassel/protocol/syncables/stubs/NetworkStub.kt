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
import de.justjanne.libquassel.protocol.models.NetworkInfo
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

@SyncedObject("Network")
interface NetworkStub : SyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNetworkName(networkName: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNetworkName",
      /**
       * Construct a QVariant from a String
       */
      qVariant(networkName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCurrentServer(currentServer: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCurrentServer",
      /**
       * Construct a QVariant from a String
       */
      qVariant(currentServer, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMyNick(myNick: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMyNick",
      /**
       * Construct a QVariant from a String
       */
      qVariant(myNick, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setLatency(latency: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setLatency",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(latency, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCodecForServer(codecForServer: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCodecForServer",
      /**
       * Construct a QVariant from a ByteBuffer
       */
      qVariant(codecForServer, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCodecForEncoding(codecForEncoding: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCodecForEncoding",
      /**
       * Construct a QVariant from a ByteBuffer
       */
      qVariant(codecForEncoding, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCodecForDecoding(codecForDecoding: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCodecForDecoding",
      /**
       * Construct a QVariant from a ByteBuffer
       */
      qVariant(codecForDecoding, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setIdentity(identityId: IdentityId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setIdentity",
      /**
       * Construct a QVariant from a IdentityId
       */
      qVariant(identityId, QuasselType.IdentityId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setConnected(isConnected: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setConnected",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(isConnected, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setConnectionState(connectionState: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setConnectionState",
      /**
       * Construct a QVariant from a Int
       */
      qVariant(connectionState, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseRandomServer(useRandomServer: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseRandomServer",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(useRandomServer, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setPerform(perform: QStringList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setPerform",
      /**
       * Construct a QVariant from a QStringList
       */
      qVariant(perform, QtType.QStringList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSkipCaps(skipCaps: QStringList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSkipCaps",
      /**
       * Construct a QVariant from a QStringList
       */
      qVariant(skipCaps, QtType.QStringList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseAutoIdentify(useAutoIdentify: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseAutoIdentify",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(useAutoIdentify, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoIdentifyService(autoIdentifyService: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoIdentifyService",
      /**
       * Construct a QVariant from a String
       */
      qVariant(autoIdentifyService, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoIdentifyPassword(autoIdentifyPassword: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoIdentifyPassword",
      /**
       * Construct a QVariant from a String
       */
      qVariant(autoIdentifyPassword, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseSasl(useSasl: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseSasl",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(useSasl, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSaslAccount(saslAccount: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSaslAccount",
      /**
       * Construct a QVariant from a String
       */
      qVariant(saslAccount, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSaslPassword(saslPassword: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSaslPassword",
      /**
       * Construct a QVariant from a String
       */
      qVariant(saslPassword, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseAutoReconnect(useAutoReconnect: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseAutoReconnect",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(useAutoReconnect, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoReconnectInterval(autoReconnectInterval: UInt) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoReconnectInterval",
      /**
       * Construct a QVariant from a UInt
       */
      qVariant(autoReconnectInterval, QtType.UInt),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoReconnectRetries(autoReconnectRetries: UShort) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoReconnectRetries",
      /**
       * Construct a QVariant from a UShort
       */
      qVariant(autoReconnectRetries, QtType.UShort),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUnlimitedReconnectRetries(unlimitedReconnectRetries: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUnlimitedReconnectRetries",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(unlimitedReconnectRetries, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setRejoinChannels(rejoinChannels: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setRejoinChannels",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(rejoinChannels, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseCustomMessageRate(useCustomMessageRate: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseCustomMessageRate",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(useCustomMessageRate, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMessageRateBurstSize(messageRateBurstSize: UInt) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMessageRateBurstSize",
      /**
       * Construct a QVariant from a UInt
       */
      qVariant(messageRateBurstSize, QtType.UInt),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMessageRateDelay(messageRateDelay: UInt) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMessageRateDelay",
      /**
       * Construct a QVariant from a UInt
       */
      qVariant(messageRateDelay, QtType.UInt),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUnlimitedMessageRate(unlimitedMessageRate: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUnlimitedMessageRate",
      /**
       * Construct a QVariant from a Boolean
       */
      qVariant(unlimitedMessageRate, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setServerList(serverList: QVariantList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setServerList",
      /**
       * Construct a QVariant from a QVariantList
       */
      qVariant(serverList, QtType.QVariantList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addSupport(param: String, value: String = "") {
    sync(
      target = ProtocolSide.CLIENT,
      "addSupport",
      /**
       * Construct a QVariant from a String
       */
      qVariant(param, QtType.QString),
      /**
       * Construct a QVariant from a String
       */
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeSupport(param: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeSupport",
      /**
       * Construct a QVariant from a String
       */
      qVariant(param, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addCap(capability: String, value: String = "") {
    sync(
      target = ProtocolSide.CLIENT,
      "addCap",
      /**
       * Construct a QVariant from a String
       */
      qVariant(capability, QtType.QString),
      /**
       * Construct a QVariant from a String
       */
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun acknowledgeCap(param: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "acknowledgeCap",
      /**
       * Construct a QVariant from a String
       */
      qVariant(param, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeCap(param: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeCap",
      /**
       * Construct a QVariant from a String
       */
      qVariant(param, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun clearCaps() {
    sync(
      target = ProtocolSide.CLIENT,
      "clearCaps"
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addIrcUser(hostmask: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "addIrcUser",
      /**
       * Construct a QVariant from a String
       */
      qVariant(hostmask, QtType.QString),
    )
  }
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addIrcChannel(channel: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "addIrcChannel",
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestConnect() {
    sync(
      target = ProtocolSide.CLIENT,
      "requestConnect",
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestDisconnect() {
    sync(
      target = ProtocolSide.CLIENT,
      "requestDisconnect",
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetNetworkInfo(info: NetworkInfo) {
    sync(
      target = ProtocolSide.CLIENT,
      "requestSetNetworkInfo",
      qVariant(info, QuasselType.NetworkInfo),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
