/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.client.session

import de.justjanne.libquassel.client.util.CoroutineQueue
import de.justjanne.libquassel.protocol.syncables.ObjectIdentifier
import de.justjanne.libquassel.protocol.syncables.SyncableStub
import de.justjanne.libquassel.protocol.util.update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class BaseInitHandler(
  private val session: ClientSession
) {
  private val coroutineQueue = CoroutineQueue<Unit>()

  fun sync(stub: SyncableStub) {
    if (!stub.initialized) {
      state.update {
        copy(started = true, total = total + 1, waiting = waiting + ObjectIdentifier(stub))
      }
    }
    session.proxy.synchronize(stub)
  }

  suspend fun initialized(identifier: ObjectIdentifier) {
    state.update {
      copy(waiting = waiting - identifier)
    }
    if (initDone()) {
      coroutineQueue.resume(Unit)
    }
  }

  fun initDone() = state().started && state().waiting.isEmpty()

  suspend fun waitForInitDone() = if (!initDone()) {
    coroutineQueue.wait()
  } else Unit

  @Suppress("NOTHING_TO_INLINE")
  inline fun state(): BaseInitHandlerState = state.value

  @Suppress("NOTHING_TO_INLINE")
  inline fun flow(): Flow<BaseInitHandlerState> = state

  @PublishedApi
  internal val state = MutableStateFlow(BaseInitHandlerState())
}
