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

import de.justjanne.libquassel.protocol.models.ChannelModes
import de.justjanne.libquassel.protocol.models.ids.NetworkId

data class IrcChannelState(
  val network: NetworkId,
  val name: String,
  val topic: String = "",
  val password: String = "",
  val encrypted: Boolean = false,
  val channelModes: ChannelModes = ChannelModes(),
  val userModes: Map<String, Set<Char>> = emptyMap()
)
