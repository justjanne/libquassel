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
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.network.NetworkInfo
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableStub
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import java.nio.ByteBuffer

@SyncedObject("Network")
interface NetworkStub : StatefulSyncableStub {
  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setNetworkName(networkName: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setNetworkName",
      qVariant(networkName, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCurrentServer(currentServer: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCurrentServer",
      qVariant(currentServer, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMyNick(myNick: String?) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMyNick",
      qVariant(myNick, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setLatency(latency: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setLatency",
      qVariant(latency, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCodecForServer(codecForServer: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCodecForServer",
      qVariant(codecForServer, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCodecForEncoding(codecForEncoding: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCodecForEncoding",
      qVariant(codecForEncoding, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setCodecForDecoding(codecForDecoding: ByteBuffer) {
    sync(
      target = ProtocolSide.CLIENT,
      "setCodecForDecoding",
      qVariant(codecForDecoding, QtType.QByteArray),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setIdentity(identityId: IdentityId) {
    sync(
      target = ProtocolSide.CLIENT,
      "setIdentity",
      qVariant(identityId, QuasselType.IdentityId),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setConnected(isConnected: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setConnected",
      qVariant(isConnected, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setConnectionState(connectionState: Int) {
    sync(
      target = ProtocolSide.CLIENT,
      "setConnectionState",
      qVariant(connectionState, QtType.Int),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseRandomServer(useRandomServer: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseRandomServer",
      qVariant(useRandomServer, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setPerform(perform: QStringList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setPerform",
      qVariant(perform, QtType.QStringList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSkipCaps(skipCaps: QStringList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSkipCaps",
      qVariant(skipCaps, QtType.QStringList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseAutoIdentify(useAutoIdentify: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseAutoIdentify",
      qVariant(useAutoIdentify, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoIdentifyService(autoIdentifyService: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoIdentifyService",
      qVariant(autoIdentifyService, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoIdentifyPassword(autoIdentifyPassword: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoIdentifyPassword",
      qVariant(autoIdentifyPassword, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseSasl(useSasl: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseSasl",
      qVariant(useSasl, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSaslAccount(saslAccount: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSaslAccount",
      qVariant(saslAccount, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setSaslPassword(saslPassword: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "setSaslPassword",
      qVariant(saslPassword, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseAutoReconnect(useAutoReconnect: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseAutoReconnect",
      qVariant(useAutoReconnect, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoReconnectInterval(autoReconnectInterval: UInt) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoReconnectInterval",
      qVariant(autoReconnectInterval, QtType.UInt),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setAutoReconnectRetries(autoReconnectRetries: UShort) {
    sync(
      target = ProtocolSide.CLIENT,
      "setAutoReconnectRetries",
      qVariant(autoReconnectRetries, QtType.UShort),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUnlimitedReconnectRetries(unlimitedReconnectRetries: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUnlimitedReconnectRetries",
      qVariant(unlimitedReconnectRetries, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setRejoinChannels(rejoinChannels: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setRejoinChannels",
      qVariant(rejoinChannels, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUseCustomMessageRate(useCustomMessageRate: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUseCustomMessageRate",
      qVariant(useCustomMessageRate, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMessageRateBurstSize(messageRateBurstSize: UInt) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMessageRateBurstSize",
      qVariant(messageRateBurstSize, QtType.UInt),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setMessageRateDelay(messageRateDelay: UInt) {
    sync(
      target = ProtocolSide.CLIENT,
      "setMessageRateDelay",
      qVariant(messageRateDelay, QtType.UInt),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setUnlimitedMessageRate(unlimitedMessageRate: Boolean) {
    sync(
      target = ProtocolSide.CLIENT,
      "setUnlimitedMessageRate",
      qVariant(unlimitedMessageRate, QtType.Bool),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun setServerList(serverList: QVariantList) {
    sync(
      target = ProtocolSide.CLIENT,
      "setServerList",
      qVariant(serverList, QtType.QVariantList),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addSupport(param: String, value: String = "") {
    sync(
      target = ProtocolSide.CLIENT,
      "addSupport",
      qVariant(param, QtType.QString),
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeSupport(param: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeSupport",
      qVariant(param, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun addCap(capability: String, value: String = "") {
    sync(
      target = ProtocolSide.CLIENT,
      "addCap",
      qVariant(capability, QtType.QString),
      qVariant(value, QtType.QString),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun acknowledgeCap(capability: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "acknowledgeCap",
      qVariant(capability, QtType.QString)
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  fun removeCap(capability: String) {
    sync(
      target = ProtocolSide.CLIENT,
      "removeCap",
      qVariant(capability, QtType.QString)
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
      target = ProtocolSide.CORE,
      "requestConnect",
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestDisconnect() {
    sync(
      target = ProtocolSide.CORE,
      "requestDisconnect",
    )
  }

  @SyncedCall(target = ProtocolSide.CORE)
  fun requestSetNetworkInfo(info: NetworkInfo) {
    sync(
      target = ProtocolSide.CORE,
      "requestSetNetworkInfo",
      qVariant(info, QuasselType.NetworkInfo),
    )
  }

  @SyncedCall(target = ProtocolSide.CLIENT)
  override fun update(properties: QVariantMap) = super.update(properties)

  @SyncedCall(target = ProtocolSide.CORE)
  override fun requestUpdate(properties: QVariantMap) = super.requestUpdate(properties)
}
