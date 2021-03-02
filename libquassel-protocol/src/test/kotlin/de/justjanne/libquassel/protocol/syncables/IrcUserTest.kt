/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.syncables.state.IrcUserState
import de.justjanne.libquassel.protocol.testutil.nextIrcUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.threeten.bp.Instant
import kotlin.random.Random

class IrcUserTest {
  @Test
  fun testEmpty() {
    val state = IrcUserState(
      network = NetworkId(1),
      nick = "nick",
      user = "user",
      host = "host"
    )
    val actual = IrcUser(state = state).apply {
      update(emptyMap())
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = random.nextIrcUser(NetworkId(random.nextInt()))

    val actual = IrcUser(
      state = IrcUserState(
        network = expected.network,
        nick = expected.nick,
        user = expected.user,
        host = expected.host
      )
    ).apply {
      update(IrcUser(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class Setters {
    @Test
    fun testHostMask() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      val nick = user.nick()
      assertNotEquals("$nick!user@host", user.hostMask())
      user.updateHostmask("$nick!user@host")
      assertEquals("$nick!user@host", user.hostMask())
    }

    @Test
    fun testAddUserModes() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      user.setUserModes("abc")
      assertEquals(setOf('a', 'b', 'c'), user.userModes())
      user.addUserModes("ef")
      assertEquals(setOf('a', 'b', 'c', 'e', 'f'), user.userModes())
    }

    @Test
    fun testRemoveUserModes() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      user.setUserModes("abc")
      assertEquals(setOf('a', 'b', 'c'), user.userModes())
      user.removeUserModes("ac")
      assertEquals(setOf('b'), user.userModes())
    }

    @Test
    fun testUserUnverified() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("~newuser", user.user())
      assertNotEquals(null, user.verifiedUser())
      user.setUser("~newuser")
      assertEquals("~newuser", user.user())
      assertEquals(null, user.verifiedUser())
    }

    @Test
    fun testUserVerified() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("newuser", user.user())
      assertNotEquals("newuser", user.verifiedUser())
      user.setUser("newuser")
      assertEquals("newuser", user.user())
      assertEquals("newuser", user.verifiedUser())
    }

    @Test
    fun testHost() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("TeraPro33-41.LowerMyBills.com", user.host())
      user.setHost("TeraPro33-41.LowerMyBills.com")
      assertEquals("TeraPro33-41.LowerMyBills.com", user.host())
    }

    @Test
    fun testRealName() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("Bruce Wayne", user.realName())
      user.setRealName("Bruce Wayne")
      assertEquals("Bruce Wayne", user.realName())
    }

    @Test
    fun testAccount() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("thebatman", user.account())
      user.setAccount("thebatman")
      assertEquals("thebatman", user.account())
    }

    @Test
    fun testAway() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      user.setAway(false)
      assertEquals(false, user.isAway())
      user.setAway(true)
      assertEquals(true, user.isAway())
    }

    @Test
    fun testAwayMessage() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("I’ll be back", user.awayMessage())
      user.setAwayMessage("I’ll be back")
      assertEquals("I’ll be back", user.awayMessage())
    }

    @Test
    fun testIdleTime() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      val timestamp = Instant.ofEpochSecond(1614642922)
      assertNotEquals(timestamp, user.idleTime())
      user.setIdleTime(timestamp)
      assertEquals(timestamp, user.idleTime())
    }

    @Test
    fun testLoginTime() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      val timestamp = Instant.ofEpochSecond(1614642922)
      assertNotEquals(timestamp, user.loginTime())
      user.setLoginTime(timestamp)
      assertEquals(timestamp, user.loginTime())
    }

    @Test
    fun testIrcOperator() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("lorem ipsum i dolor sit amet", user.ircOperator())
      user.setIrcOperator("lorem ipsum i dolor sit amet")
      assertEquals("lorem ipsum i dolor sit amet", user.ircOperator())
    }

    @Test
    fun testLastAwayMessage() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      val timestamp = Instant.ofEpochSecond(1614642922)
      assertNotEquals(timestamp, user.lastAwayMessageTime())
      user.setLastAwayMessage(timestamp.epochSecond.toInt())
      assertEquals(timestamp, user.lastAwayMessageTime())
    }

    @Test
    fun testLastAwayMessageTime() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      val timestamp = Instant.ofEpochSecond(1614642922)
      assertNotEquals(timestamp, user.lastAwayMessageTime())
      user.setLastAwayMessageTime(timestamp)
      assertEquals(timestamp, user.lastAwayMessageTime())
    }

    @Test
    fun testWhoisServiceReply() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("lorem ipsum i dolor sit amet", user.whoisServiceReply())
      user.setWhoisServiceReply("lorem ipsum i dolor sit amet")
      assertEquals("lorem ipsum i dolor sit amet", user.whoisServiceReply())
    }

    @Test
    fun testSuserHost() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("lorem ipsum i dolor sit amet", user.suserHost())
      user.setSuserHost("lorem ipsum i dolor sit amet")
      assertEquals("lorem ipsum i dolor sit amet", user.suserHost())
    }

    @Test
    fun testEncrypted() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      user.setEncrypted(false)
      assertEquals(false, user.isEncrypted())
      user.setEncrypted(true)
      assertEquals(true, user.isEncrypted())
    }

    @Test
    fun testServer() {
      val random = Random(1337)
      val user = IrcUser(state = random.nextIrcUser(NetworkId(random.nextInt())))

      assertNotEquals("orwell.freenode.net", user.server())
      user.setServer("orwell.freenode.net")
      assertEquals("orwell.freenode.net", user.server())
    }
  }
}
