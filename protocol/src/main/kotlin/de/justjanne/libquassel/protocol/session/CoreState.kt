/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.session

import de.justjanne.libquassel.protocol.models.setup.BackendInfo

sealed class CoreState {
  object Configured : CoreState()
  data class Unconfigured(
    val databases: List<BackendInfo>,
    val authenticators: List<BackendInfo>
  ) : CoreState()
}
