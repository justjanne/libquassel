/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.network

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId

data class NetworkInfo(
  val networkId: NetworkId = NetworkId(-1),
  val networkName: String = "",
  val identity: IdentityId = IdentityId(-1),
  val useCustomEncodings: Boolean = false,
  val codecForServer: String = "UTF_8",
  val codecForEncoding: String = "UTF_8",
  val codecForDecoding: String = "UTF_8",
  val serverList: List<NetworkServer> = emptyList(),
  val useRandomServer: Boolean = false,
  val perform: List<String> = emptyList(),
  val useAutoIdentify: Boolean = false,
  val autoIdentifyService: String = "",
  val autoIdentifyPassword: String = "",
  val useSasl: Boolean = false,
  val saslAccount: String = "",
  val saslPassword: String = "",
  val useAutoReconnect: Boolean = true,
  val autoReconnectInterval: UInt = 0u,
  val autoReconnectRetries: UShort = 0u,
  val unlimitedReconnectRetries: Boolean = true,
  val rejoinChannels: Boolean = true,
  val useCustomMessageRate: Boolean = false,
  val messageRateBurstSize: UInt = 0u,
  val messageRateDelay: UInt = 0u,
  val unlimitedMessageRate: Boolean = false
)
