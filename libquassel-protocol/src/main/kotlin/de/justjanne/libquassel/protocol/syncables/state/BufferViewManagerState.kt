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

import de.justjanne.libquassel.protocol.syncables.BufferViewConfig

data class BufferViewManagerState(
  val bufferViewConfigs: Map<Int, BufferViewConfig> = emptyMap()
) {
  fun contains(bufferViewId: Int) = bufferViewConfigs.containsKey(bufferViewId)
  fun bufferViewConfig(bufferViewId: Int) = bufferViewConfigs[bufferViewId]
  fun bufferViewConfigs() = bufferViewConfigs.values
}
