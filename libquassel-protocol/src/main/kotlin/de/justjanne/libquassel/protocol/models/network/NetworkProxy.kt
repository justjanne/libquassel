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

enum class NetworkProxy(
  val value: Int,
) {
  DefaultProxy(0),
  Socks5Proxy(1),
  NoProxy(2),
  HttpProxy(3),
  HttpCachingProxy(4),
  FtpCachingProxy(5);

  companion object {
    private val values = enumValues<NetworkProxy>()
      .associateBy(NetworkProxy::value)

    /**
     * Obtain from underlying representation
     */
    fun of(value: Int): NetworkProxy? = values[value]
  }
}
