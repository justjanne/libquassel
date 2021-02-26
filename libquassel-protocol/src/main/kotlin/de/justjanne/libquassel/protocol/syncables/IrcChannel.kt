/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.ChannelModeType
import de.justjanne.libquassel.protocol.models.ChannelModes
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.IrcChannelState
import de.justjanne.libquassel.protocol.syncables.stubs.IrcChannelStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.indexed
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import kotlinx.coroutines.flow.MutableStateFlow

class IrcChannel(
  name: String,
  network: NetworkId,
  session: Session
) : SyncableObject(session, "IrcChannel"), IrcChannelStub {
  override fun init() {
    require(name().isNotEmpty()) {
      "IrcChannel: channelName is empty"
    }
    renameObject("${network().id}/${name()}")
  }

  override fun fromVariantMap(properties: QVariantMap) =
    fromVariantMap(properties, null)

  fun fromVariantMap(properties: QVariantMap, index: Int?) {
    state.update {
      copy(
        name = properties["name"].indexed(index).into(name),
        topic = properties["topic"].indexed(index).into(topic),
        password = properties["password"].indexed(index).into(password),
        encrypted = properties["encrypted"].indexed(index).into(encrypted),
        channelModes = properties["ChanModes"].indexed(index).into<QVariantMap>()
          ?.let(ChannelModes.Companion::fromVariantMap) ?: channelModes,
        userModes = properties["UserModes"].into<QVariantMap>()
          ?.mapValues { (_, value) -> value.into<String>()?.toSet().orEmpty() }
          .orEmpty()
      )
    }
  }

  override fun toVariantMap(): QVariantMap {
    return mapOf(
      "name" to qVariant(name(), QtType.QString),
      "topic" to qVariant(topic(), QtType.QString),
      "password" to qVariant(password(), QtType.QString),
      "encrypted" to qVariant(isEncrypted(), QtType.Bool),
      "ChanModes" to qVariant(state.value.channelModes.toVariantMap(), QtType.QVariantMap),
      "UserModes" to qVariant(
        state.value.userModes.mapValues { (_, value) ->
          qVariant(value.joinToString(), QtType.QString)
        },
        QtType.QVariantMap
      )
    )
  }

  fun network() = state.value.network
  fun name() = state.value.name
  fun topic() = state.value.topic
  fun password() = state.value.password
  fun isEncrypted() = state.value.encrypted
  fun ircUsers() = session.network(network())?.let { network ->
    state.value.userModes.keys.mapNotNull(network::ircUser)
  }.orEmpty()

  fun userCount() = state.value.userModes.size
  fun userModes(nick: String) = state.value.userModes[nick]
  fun hasMode(mode: Char) = when (session.network(network())?.channelModeType(mode)) {
    ChannelModeType.A_CHANMODE ->
      state.value.channelModes.a.contains(mode)
    ChannelModeType.B_CHANMODE ->
      state.value.channelModes.b.contains(mode)
    ChannelModeType.C_CHANMODE ->
      state.value.channelModes.c.contains(mode)
    ChannelModeType.D_CHANMODE ->
      state.value.channelModes.d.contains(mode)
    else ->
      false
  }

  fun modeValue(mode: Char) = when (session.network(network())?.channelModeType(mode)) {
    ChannelModeType.B_CHANMODE ->
      state.value.channelModes.b[mode] ?: ""
    ChannelModeType.C_CHANMODE ->
      state.value.channelModes.c[mode] ?: ""
    else ->
      ""
  }

  fun modeValues(mode: Char) = when (session.network(network())?.channelModeType(mode)) {
    ChannelModeType.A_CHANMODE ->
      state.value.channelModes.a[mode].orEmpty()
    else ->
      emptySet()
  }

  fun channelModeString() = state.value.channelModes.modeString()

  override fun setTopic(topic: String) {
    state.update {
      copy(topic = topic)
    }
    super.setTopic(topic)
  }

  override fun setPassword(password: String) {
    state.update {
      copy(password = password)
    }
    super.setPassword(password)
  }

  override fun setEncrypted(encrypted: Boolean) {
    state.update {
      copy(encrypted = encrypted)
    }
    super.setEncrypted(encrypted)
  }

  override fun joinIrcUsers(nicks: QStringList, modes: QStringList) {
    joinIrcUsers(
      nicks.filterNotNull().zip(modes).map { (nick, mode) ->
        Pair(nick, mode?.toSet().orEmpty())
      }.toMap()
    )
    super.joinIrcUsers(nicks, modes)
  }

  private fun joinIrcUsers(map: Map<String, Set<Char>>) {
    val network = session.network(network())

    val newNicks = map.keys - state.value.userModes.keys
    state.update {
      copy(
        userModes = userModes + map.mapValues { (key, value) ->
          value + userModes[key].orEmpty()
        }
      )
    }
    for (newNick in newNicks) {
      network
        ?.ircUser(newNick)
        ?.joinChannel(this, skipChannelJoin = true)
    }
  }

  fun joinIrcUser(user: IrcUser) = joinIrcUsers(
    mapOf(
      user.nick() to emptySet()
    )
  )

  override fun part(nick: String) {
    val network = session.network(network())
    val partingUser = network?.ircUser(nick)

    if (partingUser != null) {
      partingUser.partChannel(name())
      if (network.isMe(partingUser) || state.value.userModes.isEmpty()) {
        for (nickname in state.value.userModes.keys.toList()) {
          network.ircUser(nickname)?.partChannel(this)
        }
        state.update {
          copy(channelModes = ChannelModes())
        }
        network.removeIrcChannel(this)
        session.stopSynchronize(this)
      }
    }
    super.part(nick)
  }

  override fun setUserModes(nick: String, modes: String?) {
    state.update {
      copy(
        userModes = userModes + Pair(
          nick,
          modes?.toSet().orEmpty()
        )
      )
    }
    super.setUserModes(nick, modes)
  }

  override fun addUserMode(nick: String, mode: String?) {
    state.update {
      copy(
        userModes = userModes + Pair(
          nick,
          userModes[nick].orEmpty() + mode?.toSet().orEmpty()
        )
      )
    }
    super.addUserMode(nick, mode)
  }

  override fun removeUserMode(nick: String, mode: String?) {
    state.update {
      copy(
        userModes = userModes + Pair(
          nick,
          userModes[nick].orEmpty() - mode?.toSet().orEmpty()
        )
      )
    }
    super.addUserMode(nick, mode)
  }

  override fun addChannelMode(mode: Char, value: String?) {
    val network = session.network(network())
    state.update {
      copy(
        channelModes = channelModes.run {
          when (network?.channelModeType(mode)) {
            ChannelModeType.A_CHANMODE -> {
              requireNotNull(value) {
                "Mode $mode of ChannelModeType A must have a value"
              }

              copy(a = a + Pair(mode, a[mode].orEmpty() + value))
            }
            ChannelModeType.B_CHANMODE -> {
              requireNotNull(value) {
                "Mode $mode of ChannelModeType B must have a value"
              }

              copy(b = b + Pair(mode, value))
            }
            ChannelModeType.C_CHANMODE -> {
              requireNotNull(value) {
                "Mode $mode of ChannelModeType C must have a value"
              }

              copy(c = c + Pair(mode, value))
            }
            ChannelModeType.D_CHANMODE ->
              copy(d = d + mode)
            else -> channelModes
          }
        }
      )
    }
    super.addChannelMode(mode, value)
  }

  override fun removeChannelMode(mode: Char, value: String?) {
    val network = session.network(network())
    state.update {
      copy(
        channelModes = channelModes.run {
          when (network?.channelModeType(mode)) {
            ChannelModeType.A_CHANMODE -> {
              requireNotNull(value) {
                "Mode $mode of ChannelModeType A must have a value"
              }

              copy(a = a + Pair(mode, a[mode].orEmpty() - value))
            }
            ChannelModeType.B_CHANMODE -> {
              copy(b = b - mode)
            }
            ChannelModeType.C_CHANMODE -> {
              copy(b = c - mode)
            }
            ChannelModeType.D_CHANMODE ->
              copy(d = d - mode)
            else -> channelModes
          }
        }
      )
    }
    super.removeChannelMode(mode, value)
  }

  private val state = MutableStateFlow(
    IrcChannelState(
      network = network,
      name = name
    )
  )
}