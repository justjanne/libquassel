/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.models.network

data class NetworkServer(
  val host: String,
  val port: UInt = 6667u,
  val password: String = "",
  val useSsl: Boolean = false,
  val sslVerify: Boolean = true,
  val sslVersion: Int = 0,
  val useProxy: Boolean = false,
  val proxyType: NetworkProxy = NetworkProxy.Socks5Proxy,
  val proxyHost: String = "localhost",
  val proxyPort: UInt = 8080u,
  val proxyUser: String = "",
  val proxyPass: String = "",
)
