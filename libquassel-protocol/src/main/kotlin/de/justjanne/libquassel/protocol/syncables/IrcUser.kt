/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.IrcUserState
import de.justjanne.libquassel.protocol.syncables.stubs.IrcUserStub
import de.justjanne.libquassel.protocol.util.irc.HostmaskHelper
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.indexed
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import org.threeten.bp.Instant
import org.threeten.bp.temporal.Temporal

open class IrcUser(
  session: Session? = null,
  state: IrcUserState
) : StatefulSyncableObject<IrcUserState>(session, "IrcUser", state),
  IrcUserStub {
  init {
    renameObject(state().identifier())
  }

  override fun fromVariantMap(properties: QVariantMap) =
    fromVariantMap(properties, null)

  fun fromVariantMap(properties: QVariantMap, index: Int?) {
    state.update {
      copy(
        nick = properties["nick"].indexed(index).into(nick),
        user = properties["user"].indexed(index).into(user),
        host = properties["host"].indexed(index).into(host),
        realName = properties["realName"].indexed(index).into(realName),
        account = properties["account"].indexed(index).into(account),
        away = properties["away"].indexed(index).into(away),
        awayMessage = properties["awayMessage"].indexed(index).into(awayMessage),
        idleTime = properties["idleTime"].indexed(index).into(idleTime),
        loginTime = properties["loginTime"].indexed(index).into(loginTime),
        server = properties["server"].indexed(index).into(server),
        ircOperator = properties["ircOperator"].indexed(index).into(ircOperator),
        lastAwayMessageTime = properties["lastAwayMessageTime"].indexed(index).into()
          ?: properties["lastAwayMessage"].indexed(index).into<Int>()?.toLong()
            ?.let(Instant::ofEpochSecond)
          ?: lastAwayMessageTime,
        whoisServiceReply = properties["whoisServiceReply"].indexed(index).into(whoisServiceReply),
        suserHost = properties["suserHost"].indexed(index).into(suserHost),
        encrypted = properties["encrypted"].indexed(index).into(encrypted),
        channels = properties["channels"].indexed(index).into(channels),
        userModes = properties["userModes"].indexed(index).into(userModes),
      )
    }
    renameObject(state().identifier())
  }

  override fun toVariantMap() = mapOf(
    "nick" to qVariant(nick(), QtType.QString),
    "user" to qVariant(user(), QtType.QString),
    "host" to qVariant(host(), QtType.QString),
    "realName" to qVariant(realName(), QtType.QString),
    "account" to qVariant(account(), QtType.QString),
    "away" to qVariant(isAway(), QtType.Bool),
    "awayMessage" to qVariant(awayMessage(), QtType.QString),
    "idleTime" to qVariant(idleTime(), QtType.QDateTime),
    "loginTime" to qVariant(loginTime(), QtType.QDateTime),
    "server" to qVariant(server(), QtType.QString),
    "ircOperator" to qVariant(ircOperator(), QtType.QString),
    "lastAwayMessage" to qVariant(lastAwayMessageTime().epochSecond.toInt(), QtType.Int),
    "lastAwayMessageTime" to qVariant(lastAwayMessageTime(), QtType.QDateTime),
    "whoisServiceReply" to qVariant(whoisServiceReply(), QtType.QString),
    "suserHost" to qVariant(suserHost(), QtType.QString),
    "encrypted" to qVariant(encrypted(), QtType.Bool),

    "channels" to qVariant(channels().toList(), QtType.QStringList),
    "userModes" to qVariant(userModes().joinToString(), QtType.QString)
  )

  override fun updateHostmask(mask: String) {
    state.update {
      val (_, user, host) = HostmaskHelper.split(mask)
      copy(user = user, host = host)
    }
    super.updateHostmask(mask)
  }

  override fun addUserModes(modes: String) {
    state.update {
      copy(userModes = userModes + modes.toSet())
    }
    super.addUserModes(modes)
  }

  override fun removeUserModes(modes: String) {
    state.update {
      copy(userModes = userModes - modes.toSet())
    }
    super.removeUserModes(modes)
  }

  override fun setUser(user: String) {
    state.update {
      copy(user = user)
    }
    super.setUser(user)
  }

  override fun setHost(host: String) {
    state.update {
      copy(host = host)
    }
    super.setHost(host)
  }

  override fun setNick(nick: String) {
    val network = session?.network(network())
    network?.ircUserNickChanged(nick(), nick)
    state.update {
      copy(nick = nick)
    }
    renameObject(state().identifier())
    super.setNick(nick)
  }

  override fun setRealName(realName: String) {
    state.update {
      copy(realName = realName)
    }
    super.setRealName(realName)
  }

  override fun setAccount(account: String) {
    state.update {
      copy(account = account)
    }
    super.setAccount(account)
  }

  override fun setAway(away: Boolean) {
    state.update {
      copy(away = away)
    }
    super.setAway(away)
  }

  override fun setAwayMessage(awayMessage: String) {
    state.update {
      copy(awayMessage = awayMessage)
    }
    super.setAwayMessage(awayMessage)
  }

  override fun setIdleTime(idleTime: Temporal) {
    state.update {
      copy(idleTime = Instant.from(idleTime))
    }
    super.setIdleTime(idleTime)
  }

  override fun setLoginTime(loginTime: Temporal) {
    state.update {
      copy(loginTime = Instant.from(loginTime))
    }
    super.setLoginTime(loginTime)
  }

  override fun setIrcOperator(ircOperator: String) {
    state.update {
      copy(ircOperator = ircOperator)
    }
    super.setIrcOperator(ircOperator)
  }

  override fun setLastAwayMessage(lastAwayMessage: Int) {
    state.update {
      copy(lastAwayMessageTime = Instant.ofEpochSecond(lastAwayMessage.toLong()))
    }
    super.setLastAwayMessage(lastAwayMessage)
  }

  override fun setLastAwayMessageTime(lastAwayMessageTime: Temporal) {
    state.update {
      copy(lastAwayMessageTime = Instant.from(lastAwayMessageTime))
    }
    super.setLastAwayMessageTime(lastAwayMessageTime)
  }

  override fun setWhoisServiceReply(whoisServiceReply: String) {
    state.update {
      copy(whoisServiceReply = whoisServiceReply)
    }
    super.setWhoisServiceReply(whoisServiceReply)
  }

  override fun setSuserHost(suserHost: String) {
    state.update {
      copy(suserHost = suserHost)
    }
    super.setSuserHost(suserHost)
  }

  override fun setEncrypted(encrypted: Boolean) {
    state.update {
      copy(encrypted = encrypted)
    }
    super.setEncrypted(encrypted)
  }

  override fun setServer(server: String) {
    state.update {
      copy(server = server)
    }
    super.setServer(server)
  }

  override fun setUserModes(modes: String) {
    state.update {
      copy(userModes = userModes.toSet())
    }
    super.setUserModes(modes)
  }

  fun joinChannel(channel: IrcChannel, skipChannelJoin: Boolean = false) {
    if (state().channels.contains(channel.name())) {
      return
    }

    state.update {
      copy(channels = channels + channel.name())
    }
    if (!skipChannelJoin) {
      channel.joinIrcUser(this)
    }
    super.joinChannel(channel.name())
  }

  override fun joinChannel(channelname: String) {
    val network = session?.network(network()) ?: return
    val channel = network.newIrcChannel(channelname)
    joinChannel(channel)
  }

  fun partChannel(channel: IrcChannel) {
    val network = session?.network(network())

    state.update {
      copy(channels = channels - channel.name())
    }
    channel.part(nick())
    super.partChannel(channel.name())
    if (channels().isEmpty() && network?.isMe(this) != true) {
      quit()
    }
  }

  override fun quit() {
    val network = session?.network(network())
    for (channel in channels()) {
      network?.ircChannel(channel)
        ?.part(nick())
    }
    state.update {
      copy(channels = emptySet())
    }
    network?.removeIrcUser(this)
    session?.stopSynchronize(this)
    super.quit()
  }

  fun network() = state().network
  fun nick() = state().nick
  fun user() = state().user
  fun verifiedUser() = state().verifiedUser()
  fun host() = state().host
  fun realName() = state().realName
  fun account() = state().account
  fun hostMask() = state().hostMask()
  fun isAway() = state().away
  fun awayMessage() = state().awayMessage
  fun server() = state().server
  fun idleTime() = state().idleTime

  fun loginTime() = state().loginTime
  fun ircOperator() = state().ircOperator
  fun lastAwayMessageTime() = state().lastAwayMessageTime
  fun whoisServiceReply() = state().whoisServiceReply
  fun suserHost() = state().suserHost
  fun encrypted() = state().encrypted
  fun userModes() = state().userModes
  fun channels() = state().channels
}
