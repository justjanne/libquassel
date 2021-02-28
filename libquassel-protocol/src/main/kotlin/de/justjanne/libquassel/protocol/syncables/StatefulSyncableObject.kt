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

import kotlinx.coroutines.flow.MutableStateFlow

abstract class StatefulSyncableObject<T>(
  session: Session?,
  className: String,
  state: T
) : SyncableObject(session, className) {
  override fun toString(): String {
    return "$className(objectName=$objectName, state=${state()})"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is StatefulSyncableObject<*>) return false
    if (!super.equals(other)) return false

    if (state() != other.state()) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + state().hashCode()
    return result
  }

  @Suppress("NOTHING_TO_INLINE")
  inline fun state() = flow().value

  @Suppress("NOTHING_TO_INLINE")
  inline fun flow() = state

  @PublishedApi
  internal val state = MutableStateFlow(state)
}
