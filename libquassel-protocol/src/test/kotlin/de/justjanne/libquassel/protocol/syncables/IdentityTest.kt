/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.syncables.common.Identity
import de.justjanne.libquassel.protocol.syncables.state.IdentityState
import de.justjanne.libquassel.protocol.testutil.nextIdentity
import de.justjanne.libquassel.protocol.testutil.nextString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.random.Random

class IdentityTest {
  @Test
  fun testEmpty() {
    val actual = Identity().apply {
      update(emptyMap())
    }.state()

    assertEquals(IdentityState(), actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextIdentity()

    val actual = Identity().apply {
      update(Identity(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class Setters {
    @Test
    fun testNicks() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val nicks = List(random.nextInt(20)) {
        random.nextString()
      }
      assertNotEquals(nicks, identity.nicks())
      identity.setNicks(nicks)
      assertEquals(nicks, identity.nicks())
    }

    @Test
    fun testNicksInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val nick = random.nextString()
      assertNotEquals(listOf(nick, ""), identity.nicks())
      assertNotEquals(listOf(nick, null), identity.nicks())
      identity.setNicks(listOf(nick, null))
      assertEquals(listOf(nick, ""), identity.nicks())
    }

    @Test
    fun testAutoAwayReason() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.autoAwayReason())
      identity.setAutoAwayReason(value)
      assertEquals(value, identity.autoAwayReason())
    }

    @Test
    fun testAutoAwayReasonInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.autoAwayReason())
      assertNotEquals(null, identity.autoAwayReason())
      identity.setAutoAwayReason(null)
      assertEquals("", identity.autoAwayReason())
    }

    @Test
    fun testAwayNick() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.awayNick())
      identity.setAwayNick(value)
      assertEquals(value, identity.awayNick())
    }

    @Test
    fun testAwayNickInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.awayNick())
      assertNotEquals(null, identity.awayNick())
      identity.setAwayNick(null)
      assertEquals("", identity.awayNick())
    }

    @Test
    fun testAwayReason() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.awayReason())
      identity.setAwayReason(value)
      assertEquals(value, identity.awayReason())
    }

    @Test
    fun testAwayReasonInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.awayReason())
      assertNotEquals(null, identity.awayReason())
      identity.setAwayReason(null)
      assertEquals("", identity.awayReason())
    }

    @Test
    fun testDetachAwayReason() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.detachAwayReason())
      identity.setDetachAwayReason(value)
      assertEquals(value, identity.detachAwayReason())
    }

    @Test
    fun testDetachAwayReasonInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.detachAwayReason())
      assertNotEquals(null, identity.detachAwayReason())
      identity.setDetachAwayReason(null)
      assertEquals("", identity.detachAwayReason())
    }

    @Test
    fun testIdent() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.ident())
      identity.setIdent(value)
      assertEquals(value, identity.ident())
    }

    @Test
    fun testIdentInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.ident())
      assertNotEquals(null, identity.ident())
      identity.setIdent(null)
      assertEquals("", identity.ident())
    }

    @Test
    fun testIdentityName() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.identityName())
      identity.setIdentityName(value)
      assertEquals(value, identity.identityName())
    }

    @Test
    fun testIdentityNameInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.identityName())
      assertNotEquals(null, identity.identityName())
      identity.setIdentityName(null)
      assertEquals("", identity.identityName())
    }

    @Test
    fun testKickReason() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.kickReason())
      identity.setKickReason(value)
      assertEquals(value, identity.kickReason())
    }

    @Test
    fun testKickReasonInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.kickReason())
      assertNotEquals(null, identity.kickReason())
      identity.setKickReason(null)
      assertEquals("", identity.kickReason())
    }

    @Test
    fun testPartReason() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.partReason())
      identity.setPartReason(value)
      assertEquals(value, identity.partReason())
    }

    @Test
    fun testPartReasonInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.partReason())
      assertNotEquals(null, identity.partReason())
      identity.setPartReason(null)
      assertEquals("", identity.partReason())
    }

    @Test
    fun testQuitReason() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.quitReason())
      identity.setQuitReason(value)
      assertEquals(value, identity.quitReason())
    }

    @Test
    fun testQuitReasonInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.quitReason())
      assertNotEquals(null, identity.quitReason())
      identity.setQuitReason(null)
      assertEquals("", identity.quitReason())
    }

    @Test
    fun testRealName() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextString()
      assertNotEquals(value, identity.realName())
      identity.setRealName(value)
      assertEquals(value, identity.realName())
    }

    @Test
    fun testRealNameInvalid() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      assertNotEquals("", identity.realName())
      assertNotEquals(null, identity.realName())
      identity.setRealName(null)
      assertEquals("", identity.realName())
    }

    @Test
    fun testAutoAwayEnabled() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      identity.setAutoAwayEnabled(false)
      assertEquals(false, identity.autoAwayEnabled())
      identity.setAutoAwayEnabled(true)
      assertEquals(true, identity.autoAwayEnabled())
    }

    @Test
    fun testAutoAwayReasonEnabled() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      identity.setAutoAwayReasonEnabled(false)
      assertEquals(false, identity.autoAwayReasonEnabled())
      identity.setAutoAwayReasonEnabled(true)
      assertEquals(true, identity.autoAwayReasonEnabled())
    }

    @Test
    fun testAwayNickEnabled() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      identity.setAwayNickEnabled(false)
      assertEquals(false, identity.awayNickEnabled())
      identity.setAwayNickEnabled(true)
      assertEquals(true, identity.awayNickEnabled())
    }

    @Test
    fun testAwayReasonEnabled() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      identity.setAwayReasonEnabled(false)
      assertEquals(false, identity.awayReasonEnabled())
      identity.setAwayReasonEnabled(true)
      assertEquals(true, identity.awayReasonEnabled())
    }

    @Test
    fun testDetachAwayReasonEnabled() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      identity.setDetachAwayReasonEnabled(false)
      assertEquals(false, identity.detachAwayReasonEnabled())
      identity.setDetachAwayReasonEnabled(true)
      assertEquals(true, identity.detachAwayReasonEnabled())
    }

    @Test
    fun testDetachAwayEnabled() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      identity.setDetachAwayEnabled(false)
      assertEquals(false, identity.detachAwayEnabled())
      identity.setDetachAwayEnabled(true)
      assertEquals(true, identity.detachAwayEnabled())
    }

    @Test
    fun testAutoAwayTime() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = random.nextInt()
      assertNotEquals(value, identity.autoAwayTime())
      identity.setAutoAwayTime(value)
      assertEquals(value, identity.autoAwayTime())
    }

    @Test
    fun testId() {
      val random = Random(1337)
      val identity = Identity(state = random.nextIdentity())

      val value = IdentityId(random.nextInt())
      assertNotEquals(value, identity.id())
      identity.setId(value)
      assertEquals(value, identity.id())
    }
  }
}
