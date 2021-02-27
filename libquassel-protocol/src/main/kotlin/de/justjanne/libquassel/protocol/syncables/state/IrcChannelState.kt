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

import de.justjanne.libquassel.protocol.models.ChannelModeType
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
) {
  fun channelModeString() = channelModes.modeString()

  fun ircUsers(networkState: NetworkState?) = networkState?.let { network ->
    userModes.keys.mapNotNull(network::ircUser)
  }.orEmpty()

  fun userCount() = userModes.size
  fun userModes(nick: String) = userModes[nick]
  fun hasMode(networkState: NetworkState?, mode: Char) = when (networkState?.channelModeType(mode)) {
    ChannelModeType.A_CHANMODE ->
      channelModes.a.contains(mode)
    ChannelModeType.B_CHANMODE ->
      channelModes.b.contains(mode)
    ChannelModeType.C_CHANMODE ->
      channelModes.c.contains(mode)
    ChannelModeType.D_CHANMODE ->
      channelModes.d.contains(mode)
    else ->
      false
  }

  fun modeValue(networkState: NetworkState?, mode: Char) = when (networkState?.channelModeType(mode)) {
    ChannelModeType.B_CHANMODE ->
      channelModes.b[mode] ?: ""
    ChannelModeType.C_CHANMODE ->
      channelModes.c[mode] ?: ""
    else ->
      ""
  }

  fun modeValues(networkState: NetworkState?, mode: Char) = when (networkState?.channelModeType(mode)) {
    ChannelModeType.A_CHANMODE ->
      channelModes.a[mode].orEmpty()
    else ->
      emptySet()
  }
}
