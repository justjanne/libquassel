/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.testutil.mocks

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.session.ProxyMessageHandler

open class EmptyProxyMessageHandler : ProxyMessageHandler {
  override fun emit(message: SignalProxyMessage) = Unit
  override fun dispatch(message: SignalProxyMessage) = Unit
}
