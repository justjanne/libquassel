/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil

import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.IrcChannel
import de.justjanne.libquassel.protocol.syncables.IrcUser
import de.justjanne.libquassel.protocol.syncables.state.IrcChannelState
import de.justjanne.libquassel.protocol.syncables.state.IrcUserState
import org.threeten.bp.Instant
import java.util.EnumSet
import java.util.UUID
import kotlin.random.Random

fun Random.nextString(): String {
  return UUID(nextLong(), nextLong()).toString()
}

fun <T> Random.nextOf(elements: Collection<T>): T {
  val index = nextInt(elements.size)
  return elements.drop(index).first()
}

fun <T> Random.nextOf(vararg elements: T): T {
  return nextOf(elements.toList())
}

inline fun <reified T: Enum<T>> Random.nextEnum(): T {
  return nextOf(EnumSet.allOf(T::class.java).toList())
}

fun Random.nextInstant(): Instant = Instant.ofEpochMilli(nextLong())

fun Random.nextIrcUser(
  networkId: NetworkId = NetworkId(nextInt())
) = IrcUser(
  state = IrcUserState(
    network = networkId,
    nick = nextString(),
    user = nextString(),
    host = nextString(),
    realName = nextString(),
    account = nextString(),
    away = nextBoolean(),
    awayMessage = nextString(),
    idleTime = nextInstant(),
    loginTime = nextInstant(),
    server = nextString(),
    ircOperator = nextString(),
    lastAwayMessageTime = nextInstant(),
    whoisServiceReply = nextString(),
    suserHost = nextString(),
    encrypted = nextBoolean()
  )
)

fun Random.nextIrcChannel(
  networkId: NetworkId = NetworkId(nextInt())
) = IrcChannel(
  state = IrcChannelState(
    network = networkId,
    name = nextString(),
    topic = nextString(),
    password = nextString(),
    encrypted = nextBoolean()
  )
)
