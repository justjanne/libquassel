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
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import kotlinx.coroutines.flow.MutableStateFlow

class ObjectRepository : StateHolder<ObjectRepositoryState> {
  fun add(syncable: SyncableStub): Boolean {
    val identifier = ObjectIdentifier(syncable)
    if (syncable is StatefulSyncableStub) {
      state.update {
        copy(
          syncables = syncables + Pair(
            identifier,
            syncable
          ),
          waiting = waiting + syncable
        )
      }
      return true
    } else {
      state.update {
        copy(
          syncables = syncables + Pair(
            identifier,
            syncable
          )
        )
      }
      return false
    }
  }

  fun init(syncable: SyncableStub, properties: QVariantMap) {
    if (syncable is StatefulSyncableStub) {
      syncable.fromVariantMap(properties)
      syncable.initialized = true
      state.update {
        copy(waiting = waiting - syncable)
      }
    }
  }

  fun rename(syncable: SyncableStub, newName: String) {
    val identifier = ObjectIdentifier(syncable)
    state.update {
      copy(
        syncables = syncables - identifier + Pair(
          identifier.copy(objectName = newName),
          syncable
        )
      )
    }
  }

  fun remove(syncable: SyncableStub) {
    val identifier = ObjectIdentifier(syncable)
    syncable.initialized = false
    state.update {
      copy(syncables = syncables - identifier)
    }
  }

  fun find(className: String, objectName: String): SyncableStub? {
    return state().syncables[ObjectIdentifier(className, objectName)]
  }

  inline fun <reified T : SyncableStub> find(objectName: String): T? {
    return find(T::class.java.simpleName, objectName) as? T
  }

  override fun state() = flow().value
  override fun flow() = state
  private val state = MutableStateFlow(ObjectRepositoryState())
}
