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

import de.justjanne.libquassel.protocol.models.DccIpDetectionMode
import de.justjanne.libquassel.protocol.models.DccPortSelectionMode
import java.net.InetAddress

data class DccConfigState(
  val dccEnabled: Boolean = false,
  val outgoingIp: InetAddress = InetAddress.getLocalHost(),
  val ipDetectionMode: DccIpDetectionMode = DccIpDetectionMode.Automatic,
  val portSelectionMode: DccPortSelectionMode = DccPortSelectionMode.Automatic,
  val minPort: UShort = 1024u,
  val maxPort: UShort = 32767u,
  val chunkSize: Int = 16,
  val sendTimeout: Int = 180,
  val usePassiveDcc: Boolean = false,
  val useFastSend: Boolean = false
)
