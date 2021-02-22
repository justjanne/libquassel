/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.expansion

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExpansionTest {
  @Test
  fun testDefaults() {
    assertEquals(
      listOf(
        Expansion.Text("/join "),
        Expansion.Parameter(0, null)
      ),
      Expansion.parse("/join $0")
    )

    assertEquals(
      listOf(
        Expansion.Text("/whois "),
        Expansion.Parameter(0, null),
        Expansion.Text(" "),
        Expansion.Parameter(0, null)
      ),
      Expansion.parse("/whois $0 $0")
    )
  }

  @Test
  fun testParameters() {
    assertEquals(
      listOf(
        Expansion.Text("/say Welcome to the support channel for the IRC client Quassel, "),
        Expansion.Parameter(1, null)
      ),
      Expansion.parse("/say Welcome to the support channel for the IRC client Quassel, \$1")
    )
    assertEquals(
      listOf(
        Expansion.Parameter(1, null),
        Expansion.Text(" "),
        Expansion.Parameter(1, Expansion.ParameterField.ACCOUNT),
        Expansion.Text(" "),
        Expansion.Parameter(1, Expansion.ParameterField.HOSTNAME),
        Expansion.Text(" "),
        Expansion.Parameter(1, Expansion.ParameterField.VERIFIED_IDENT),
        Expansion.Text(" "),
        Expansion.Parameter(1, Expansion.ParameterField.IDENT),
      ),
      Expansion.parse("\$1 \$1:account \$1:hostname \$1:identd \$1:ident")
    )
  }

  @Test
  fun testConstants() {
    assertEquals(
      listOf(
        Expansion.Text("/say I am "),
        Expansion.Constant(Expansion.ConstantField.NICK),
        Expansion.Text(", welcoming you to our channel "),
        Expansion.Constant(Expansion.ConstantField.CHANNEL),
        Expansion.Text(" on "),
        Expansion.Constant(Expansion.ConstantField.NETWORK),
        Expansion.Text(".")
      ),
      Expansion.parse("/say I am \$nick, welcoming you to our channel \$channelname on \$network.")
    )
    assertEquals(
      listOf(
        Expansion.Text("/say That’s right, I’m /the/ "),
        Expansion.Constant(Expansion.ConstantField.NICK),
        Expansion.Text(" from "),
        Expansion.Constant(Expansion.ConstantField.CHANNEL),
        Expansion.Text(".")
      ),
      Expansion.parse("/say That’s right, I’m /the/ \$nick from \$channel.")
    )
  }

  @Test
  fun testRanges() {
    assertEquals(
      listOf(
        Expansion.Text("1 \""),
        Expansion.Parameter(1, null),
        Expansion.Text("\" 2 \""),
        Expansion.Parameter(2, null),
        Expansion.Text("\" 3..4 \""),
        Expansion.ParameterRange(3, 4),
        Expansion.Text("\" 3.. \""),
        Expansion.ParameterRange(3, null),
        Expansion.Text("\""),
      ),
      Expansion.parse("1 \"\$1\" 2 \"\$2\" 3..4 \"\$3..4\" 3.. \"\$3..\"")
    )
  }
}
