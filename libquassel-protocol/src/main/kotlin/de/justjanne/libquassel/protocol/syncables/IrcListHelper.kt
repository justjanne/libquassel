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

import de.justjanne.libquassel.protocol.syncables.stubs.IrcListHelperStub
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QVariant_

open class IrcListHelper(
  session: Session? = null
) : SyncableObject(session, "IrcListHelper"), IrcListHelperStub {
  override fun fromVariantMap(properties: QVariantMap) = Unit
  override fun toVariantMap() = emptyMap<String, QVariant_>()
}
