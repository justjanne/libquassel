/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

data class NetworkConfigState(
  val pingTimeoutEnabled: Boolean = true,
  val pingInterval: Int = 30,
  val maxPingCount: Int = 6,
  val autoWhoEnabled: Boolean = true,
  val autoWhoInterval: Int = 90,
  val autoWhoNickLimit: Int = 200,
  val autoWhoDelay: Int = 5,
  val standardCtcp: Boolean = false
)
