/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.io

import de.justjanne.libquassel.client.util.TlsInfo

data class CoroutineChannelState(
  val tlsInfo: TlsInfo? = null,
  val compression: Boolean = false,
  val connected: Boolean = false,
)
