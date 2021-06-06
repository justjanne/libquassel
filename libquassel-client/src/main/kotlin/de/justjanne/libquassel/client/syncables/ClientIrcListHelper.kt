/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.syncables

import de.justjanne.libquassel.client.exceptions.IrcListException
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.common.IrcListHelper
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.into
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ClientIrcListHelper(
  session: Session
) : IrcListHelper(session) {
  private val waitingContinuations = mutableMapOf<NetworkId, Continuation<List<ChannelDescription>>>()
  private val readyContinuations = mutableMapOf<NetworkId, Continuation<List<ChannelDescription>>>()

  suspend fun channelList(
    networkId: NetworkId,
    channelFilters: List<String>
  ) = suspendCoroutine<List<ChannelDescription>> {
    waitingContinuations[networkId] = it
    requestChannelList(networkId, channelFilters)
  }

  override fun reportFinishedList(netId: NetworkId) {
    val continuation = waitingContinuations.remove(netId)
    if (continuation != null) {
      readyContinuations[netId] = continuation
      requestChannelList(netId, emptyList())
    }
    super.reportFinishedList(netId)
  }

  override fun reportError(error: String?) {
    for (continuation in waitingContinuations.values + readyContinuations.values) {
      continuation.resumeWith(Result.failure(IrcListException(error ?: "Unknown Error")))
    }
    super.reportError(error)
  }

  override fun receiveChannelList(netId: NetworkId, channelFilters: QStringList, channels: QVariantList) {
    readyContinuations[netId]?.resume(
      channels.mapNotNull {
        val list = it.into<QVariantList>().orEmpty()
        if (list.size == 3) {
          ChannelDescription(
            netId,
            list[0].into(""),
            list[1].into(0u),
            list[2].into(""),
          )
        } else {
          null
        }
      }
    )
    super.receiveChannelList(netId, channelFilters, channels)
  }

  data class ChannelDescription(
    val netId: NetworkId,
    val channelName: String,
    val userCount: UInt,
    val topic: String
  )
}
