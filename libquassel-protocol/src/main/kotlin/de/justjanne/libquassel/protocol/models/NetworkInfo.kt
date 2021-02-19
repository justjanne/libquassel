/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.NetworkId

data class NetworkInfo(
  var networkId: NetworkId = NetworkId(-1),
  var networkName: String = "",
  var identity: IdentityId = IdentityId(-1),
  var useCustomEncodings: Boolean = false,
  var codecForServer: String = "UTF_8",
  var codecForEncoding: String = "UTF_8",
  var codecForDecoding: String = "UTF_8",
  var serverList: List<NetworkServer> = emptyList(),
  var useRandomServer: Boolean = false,
  var perform: List<String> = emptyList(),
  var useAutoIdentify: Boolean = false,
  var autoIdentifyService: String = "",
  var autoIdentifyPassword: String = "",
  var useSasl: Boolean = false,
  var saslAccount: String = "",
  var saslPassword: String = "",
  var useAutoReconnect: Boolean = true,
  var autoReconnectInterval: UInt = 0u,
  var autoReconnectRetries: UShort = 0u,
  var unlimitedReconnectRetries: Boolean = true,
  var rejoinChannels: Boolean = true,
  var useCustomMessageRate: Boolean = false,
  var messageRateBurstSize: UInt = 0u,
  var messageRateDelay: UInt = 0u,
  var unlimitedMessageRate: Boolean = false
)
