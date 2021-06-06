/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.state

import de.justjanne.libquassel.protocol.models.ConnectedClient
import org.threeten.bp.Instant

data class CoreInfoState(
  val version: String = "",
  val versionDate: Instant? = null,
  val startTime: Instant = Instant.EPOCH,
  val connectedClientCount: Int = 0,
  val connectedClients: List<ConnectedClient> = emptyList()
)
