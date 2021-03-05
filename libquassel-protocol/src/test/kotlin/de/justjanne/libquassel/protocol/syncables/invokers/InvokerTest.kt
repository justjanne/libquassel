/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.invokers

import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.protocol.syncables.invoker.Invokers
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InvokerTest {
  @Test
  fun testRegistered() {
    assertEquals(
      setOf(
        "AliasManager",
        "BacklogManager",
        "BufferSyncer",
        "BufferViewConfig",
        "BufferViewManager",
        "CertManager",
        "CoreInfo",
        "DccConfig",
        "HighlightRuleManager",
        "Identity",
        "IgnoreListManager",
        "IrcChannel",
        "IrcListHelper",
        "IrcUser",
        "NetworkConfig",
        "Network",
        "RpcHandler",
        "TransferManager",
        "Transfer"
      ),
      Invokers.list(ProtocolSide.CLIENT)
    )
  }
}
