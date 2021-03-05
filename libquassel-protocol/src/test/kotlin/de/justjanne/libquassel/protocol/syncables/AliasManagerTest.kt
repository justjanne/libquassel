/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.bitflags.of
import de.justjanne.libquassel.protocol.models.BufferInfo
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.alias.Alias
import de.justjanne.libquassel.protocol.models.alias.Command
import de.justjanne.libquassel.protocol.models.flags.BufferType
import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.common.AliasManager
import de.justjanne.libquassel.protocol.syncables.state.AliasManagerState
import de.justjanne.libquassel.protocol.testutil.mocks.EmptySession
import de.justjanne.libquassel.protocol.testutil.mocks.RealisticSession
import de.justjanne.libquassel.protocol.testutil.nextAliasManager
import de.justjanne.libquassel.protocol.testutil.nextString
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class AliasManagerTest {
  @Test
  fun testEmpty() {
    val actual = AliasManager().apply {
      update(emptyMap())
    }.state()

    assertEquals(AliasManagerState(), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextAliasManager()

    val actual = AliasManager().apply {
      update(AliasManager(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Test
  fun testInvalidData() {
    val state = AliasManagerState()
    AliasManager(state = state).apply {
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "Aliases" to qVariant<QVariantMap>(
              mapOf(
                "names" to qVariant(emptyList(), QtType.QStringList),
                "expansions" to qVariant<QStringList>(listOf(""), QtType.QStringList),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
    }
  }

  @Nested
  inner class AddAlias {
    @Test
    fun new() {
      val random = Random(1337)
      val value = AliasManager(
        state = random.nextAliasManager()
      )

      val aliasCount = value.aliases().size
      val alias = Alias(random.nextString(), random.nextString())
      assertFalse(value.aliases().contains(alias))
      value.addAlias(alias.name, alias.expansion)
      assertEquals(aliasCount + 1, value.aliases().size)
      assertTrue(value.aliases().contains(alias))
      assertEquals(aliasCount, value.indexOf(alias.name))
    }

    @Test
    fun existing() {
      val random = Random(1337)
      val value = AliasManager(
        state = random.nextAliasManager()
      )

      val aliasCount = value.aliases().size
      val alias = value.aliases().first()
      assertTrue(value.aliases().contains(alias))
      value.addAlias(alias.name, alias.expansion)
      assertEquals(aliasCount, value.aliases().size)
      assertTrue(value.aliases().contains(alias))
    }
  }

  @Nested
  inner class Expansion {
    @Test
    fun plaintext() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "this is some text",
        "/SAY this is some text"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "this is some text",
        "/SAY this is some text"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "this is some text",
        "/SAY this is some text"
      )
    }

    @Test
    fun say() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/say this is some text",
        "/say this is some text"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/say this is some text",
        "/say this is some text"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/say this is some text",
        "/say this is some text"
      )
    }

    @Test
    fun userExpansionWithIdentd() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion justJanne",
        "justJanne * * * *"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion justJanne",
        "justJanne * * * *"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion justJanne",
        "justJanne justJanne kuschku.de kuschku kuschku"
      )
    }

    @Test
    fun userExpansionNoIdentd() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion digitalcircuit",
        "digitalcircuit * * * *"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion digitalcircuit",
        "digitalcircuit * * * *"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion digitalcircuit",
        "digitalcircuit digitalcircuit 2605:6000:1518:830d:ec4:7aff:fe6b:c6b0 * ~quassel"
      )
    }

    @Test
    fun userExpansionUnknownUser() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion ChanServ",
        "ChanServ * * * *"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion ChanServ",
        "ChanServ * * * *"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion ChanServ",
        "ChanServ * * * *"
      )
    }

    @Test
    fun userExpansionNoParams() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion",
        "\$1 \$1:account \$1:hostname \$1:identd \$1:ident"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion",
        "\$1 \$1:account \$1:hostname \$1:identd \$1:ident"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/userexpansion",
        "\$1 \$1:account \$1:hostname \$1:identd \$1:ident"
      )
    }

    @Test
    fun userExpansionQuery() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/userexpansion digitalcircuit",
        "digitalcircuit * * * *"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/userexpansion digitalcircuit",
        "digitalcircuit * * * *"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/userexpansion digitalcircuit",
        "digitalcircuit digitalcircuit 2605:6000:1518:830d:ec4:7aff:fe6b:c6b0 * ~quassel"
      )
    }

    @Test
    fun channelExpansionChannel() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/constantexpansion 12 3",
        "#quassel-test \$currentnick \$network 12 3"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/constantexpansion 12 3",
        "#quassel-test \$currentnick \$network 12 3"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(1),
          networkId = NetworkId(1),
          bufferName = "#quassel-test",
          type = BufferType.of(BufferType.Channel)
        ),
        "/constantexpansion 12 3",
        "#quassel-test justJanne FreeNode 12 3"
      )
    }

    @Test
    fun channelExpansionQuery() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/constantexpansion 12 3",
        "digitalcircuit \$currentnick \$network 12 3"
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/constantexpansion 12 3",
        "digitalcircuit \$currentnick \$network 12 3"
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/constantexpansion 12 3",
        "digitalcircuit justJanne FreeNode 12 3"
      )
    }

    @Test
    fun rangeExpansion() {
      testExpansion(
        null,
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/rangeexpansion a b c d e f",
        "1 \"a\" 2 \"b\" 3..4 \"c d\" 3.. \"c d e f\""
      )

      testExpansion(
        EmptySession(),
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/rangeexpansion a b c d e f",
        "1 \"a\" 2 \"b\" 3..4 \"c d\" 3.. \"c d e f\""
      )

      testExpansion(
        RealisticSession(),
        BufferInfo(
          bufferId = BufferId(3),
          networkId = NetworkId(1),
          bufferName = "digitalcircuit",
          type = BufferType.of(BufferType.Query)
        ),
        "/rangeexpansion a b c d e f",
        "1 \"a\" 2 \"b\" 3..4 \"c d\" 3.. \"c d e f\""
      )
    }

    private fun testExpansion(
      session: Session?,
      buffer: BufferInfo,
      message: String,
      expected: String
    ) {
      val value = AliasManager(
        session = session,
        state = AliasManagerState(
          aliases = listOf(
            Alias(
              "userexpansion",
              "$1 $1:account $1:hostname $1:identd $1:ident"
            ),
            Alias(
              "constantexpansion",
              "\$channel \$currentnick \$network \$0"
            ),
            Alias(
              "rangeexpansion",
              "1 \"\$1\" 2 \"\$2\" 3..4 \"\$3..4\" 3.. \"\$3..\""
            )
          )
        )
      )

      assertEquals(
        listOf(
          Command(
            buffer,
            expected
          )
        ),
        value.processInput(
          buffer,
          message
        )
      )

      assertEquals(
        listOf(
          Command(
            buffer,
            expected
          )
        ),
        mutableListOf<Command>().also {
          value.processInput(
            buffer,
            message,
            it
          )
        }
      )
    }
  }

  @Nested
  inner class DetermineMessageCommand {
    @Test
    fun plaintext() {
      assertEquals(
        Pair(null, "just some plain text"),
        AliasManagerState.determineMessageCommand("just some plain text")
      )
    }

    @Test
    fun escaped() {
      assertEquals(
        Pair(null, "/escaped content"),
        AliasManagerState.determineMessageCommand("//escaped content")
      )
    }

    @Test
    fun path() {
      assertEquals(
        Pair(null, "/bin/rm --rf /* is fun"),
        AliasManagerState.determineMessageCommand("/bin/rm --rf /* is fun")
      )
    }

    @Test
    fun fakeItalic() {
      assertEquals(
        Pair(null, "/ suuuure /"),
        AliasManagerState.determineMessageCommand("/ suuuure /")
      )
    }

    @Test
    fun command() {
      assertEquals(
        Pair("command", ""),
        AliasManagerState.determineMessageCommand("/command")
      )
    }

    @Test
    fun commandWithParam() {
      assertEquals(
        Pair("command", "parameters are nice"),
        AliasManagerState.determineMessageCommand("/command parameters are nice")
      )
    }

    @Test
    fun commandWithWhitespace() {
      assertEquals(
        Pair("command", " parameters are nice"),
        AliasManagerState.determineMessageCommand("/command  parameters are nice")
      )
    }
  }
}
