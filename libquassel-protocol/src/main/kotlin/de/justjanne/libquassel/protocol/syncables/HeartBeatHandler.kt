/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.util.StateHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.threeten.bp.Instant

class HeartBeatHandler : StateHolder<Long?> {
  private var lastReceived: Instant? = null

  /**
   * Utility function to recompute the latency value,
   * usually should be called by a timer.
   */
  fun recomputeLatency() {
    recomputeLatency(Instant.now(), force = false)
  }

  /**
   * Utility function to recompute the latency value with a given heartbeat value
   */
  fun recomputeLatency(current: Instant, force: Boolean) {
    val last = lastReceived?.toEpochMilli() ?: return
    val roundtripLatency = current.toEpochMilli() - last
    if (force || roundtripLatency > (this.roundtripLatency.value ?: return)) {
      this.roundtripLatency.value = roundtripLatency
    }
  }

  override fun flow(): Flow<Long?> = roundtripLatency
  override fun state(): Long? = roundtripLatency.value
  private val roundtripLatency = MutableStateFlow<Long?>(null)
}
