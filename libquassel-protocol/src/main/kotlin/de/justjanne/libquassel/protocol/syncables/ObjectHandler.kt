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

import de.justjanne.libquassel.protocol.variant.QVariantMap

interface ObjectRepository {
  fun register(syncable: SyncableStub, batch: Boolean)
  fun init(syncable: SyncableStub, data: QVariantMap)
  fun rename(syncable: SyncableStub, newName: String)
  fun remove(syncable: SyncableStub)

  fun <T : SyncableStub> find(type: Class<T>, objectName: String): T
}

inline fun <reified T : SyncableStub> ObjectRepository.find(objectName: String): T =
  find(T::class.java, objectName)
