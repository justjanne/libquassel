/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.common

import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.SyncableObject
import de.justjanne.libquassel.protocol.syncables.stubs.IrcListHelperStub

open class IrcListHelper(
  session: Session? = null
) : SyncableObject(session, "IrcListHelper"), IrcListHelperStub {
  init {
    initialized = true
  }
}
