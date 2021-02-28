/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.serializers.qt.StringSerializerUtf8

abstract class SyncableObject(
  override var session: Session?,
  override val className: String
) : SyncableStub {
  final override var objectName: String = ""
    internal set
  final override var initialized: Boolean = false
    internal set

  protected fun renameObject(
    newName: String
  ) {
    val oldName = objectName
    if (!initialized) {
      objectName = newName
    } else if (oldName != newName) {
      objectName = newName
      session?.objectRepository?.rename(this, newName)
      session?.objectRenamed(
        StringSerializerUtf8.serializeRaw(className),
        oldName,
        newName
      )
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SyncableObject) return false

    if (className != other.className) return false
    if (objectName != other.objectName) return false

    return true
  }

  override fun hashCode(): Int {
    var result = className.hashCode()
    result = 31 * result + objectName.hashCode()
    return result
  }
}
