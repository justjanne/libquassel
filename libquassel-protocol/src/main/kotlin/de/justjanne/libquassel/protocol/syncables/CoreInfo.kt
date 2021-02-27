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

import de.justjanne.libquassel.protocol.models.ConnectedClient
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.CoreInfoState
import de.justjanne.libquassel.protocol.syncables.stubs.CoreInfoStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QVariant_
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import kotlinx.coroutines.flow.MutableStateFlow
import org.threeten.bp.Instant

class CoreInfo constructor(
  session: Session
) : SyncableObject(session, "CoreInfo"), CoreInfoStub {
  override fun fromVariantMap(properties: QVariantMap) {
    val coreData = properties["coreData"].into<QVariantMap>().orEmpty()

    state.update {
      copy(
        version = coreData["quasselVersion"].into(version),
        versionDate = coreData["quasselBuildDate"].into("")
          .toLongOrNull()
          ?.let(Instant::ofEpochSecond),
        startTime = coreData["startTime"].into(startTime),
        connectedClientCount = coreData["sessionConnectedClients"].into(connectedClientCount),
        connectedClients = coreData["sessionConnectedClientData"].into<QVariantList>()
          ?.mapNotNull<QVariant_, QVariantMap>(QVariant_::into)
          ?.map(ConnectedClient.Companion::fromVariantMap)
          .orEmpty()
      )
    }
  }

  override fun toVariantMap() = mapOf(
    "quasselVersion" to qVariant(version(), QtType.QString),
    "quasselBuildDate" to qVariant(versionDate()?.epochSecond?.toString(), QtType.QString),
    "startTime" to qVariant(startTime(), QtType.QDateTime),
    "sessionConnectedClients" to qVariant(connectedClientCount(), QtType.Int),
    "sessionConnectedClientData" to qVariant(
      connectedClients()
        .map(ConnectedClient::toVariantMap)
        .map { qVariant(it, QtType.QVariantMap) },
      QtType.QVariantList
    )
  )

  fun version() = state().version
  fun versionDate() = state().versionDate
  fun startTime() = state().startTime
  fun connectedClientCount() = state().connectedClientCount
  fun connectedClients() = state().connectedClients

  override fun setCoreData(data: QVariantMap) {
    fromVariantMap(data)
    super.setCoreData(data)
  }

  @Suppress("NOTHING_TO_INLINE")
  inline fun state() = flow().value

  @Suppress("NOTHING_TO_INLINE")
  inline fun flow() = state

  @PublishedApi
  internal val state = MutableStateFlow(
    CoreInfoState()
  )
}
