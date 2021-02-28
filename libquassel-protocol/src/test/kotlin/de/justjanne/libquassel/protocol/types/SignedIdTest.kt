/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.justjanne.libquassel.protocol.types

import de.justjanne.libquassel.protocol.models.ids.BufferId
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.ids.MsgId
import de.justjanne.libquassel.protocol.models.ids.NetworkId
import de.justjanne.libquassel.protocol.models.ids.isValid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SignedIdTest {
  @Test
  fun testNegativeOne() {
    assertFalse(BufferId(-1).isValid())
    assertFalse(SignedIdProxy.isValidId(BufferId(-1)))
    assertFalse(IdentityId(-1).isValid())
    assertFalse(SignedIdProxy.isValidId(IdentityId(-1)))
    assertFalse(MsgId(-1).isValid())
    assertFalse(SignedIdProxy.isValidId64(MsgId(-1)))
    assertFalse(NetworkId(-1).isValid())
    assertFalse(SignedIdProxy.isValidId(NetworkId(-1)))

    assertEquals("BufferId(-1)", BufferId(-1).toString())
    assertEquals("BufferId(-1)", SignedIdProxy.toString(BufferId(-1)))
    assertEquals("IdentityId(-1)", IdentityId(-1).toString())
    assertEquals("IdentityId(-1)", SignedIdProxy.toString(IdentityId(-1)))
    assertEquals("MsgId(-1)", MsgId(-1).toString())
    assertEquals("MsgId(-1)", SignedIdProxy.toString(MsgId(-1)))
    assertEquals("NetworkId(-1)", NetworkId(-1).toString())
    assertEquals("NetworkId(-1)", SignedIdProxy.toString(NetworkId(-1)))

    assertEquals((-1).hashCode(), BufferId(-1).hashCode())
    assertEquals((-1).hashCode(), SignedIdProxy.hashCode(BufferId(-1)))
    assertEquals((-1).hashCode(), IdentityId(-1).hashCode())
    assertEquals((-1).hashCode(), SignedIdProxy.hashCode(IdentityId(-1)))
    assertEquals((-1L).hashCode(), MsgId(-1).hashCode())
    assertEquals((-1L).hashCode(), SignedIdProxy.hashCode(MsgId(-1)))
    assertEquals((-1).hashCode(), NetworkId(-1).hashCode())
    assertEquals((-1).hashCode(), SignedIdProxy.hashCode(NetworkId(-1)))
  }

  @Test
  fun testZero() {
    assertFalse(BufferId(0).isValid())
    assertFalse(SignedIdProxy.isValidId(BufferId(0)))
    assertFalse(IdentityId(0).isValid())
    assertFalse(SignedIdProxy.isValidId(IdentityId(0)))
    assertFalse(MsgId(0).isValid())
    assertFalse(SignedIdProxy.isValidId64(MsgId(0)))
    assertFalse(NetworkId(0).isValid())
    assertFalse(SignedIdProxy.isValidId(NetworkId(0)))

    assertEquals("BufferId(0)", BufferId(0).toString())
    assertEquals("BufferId(0)", SignedIdProxy.toString(BufferId(0)))
    assertEquals("IdentityId(0)", IdentityId(0).toString())
    assertEquals("IdentityId(0)", SignedIdProxy.toString(IdentityId(0)))
    assertEquals("MsgId(0)", MsgId(0).toString())
    assertEquals("MsgId(0)", SignedIdProxy.toString(MsgId(0)))
    assertEquals("NetworkId(0)", NetworkId(0).toString())
    assertEquals("NetworkId(0)", SignedIdProxy.toString(NetworkId(0)))

    assertEquals(0.hashCode(), BufferId(0).hashCode())
    assertEquals(0.hashCode(), SignedIdProxy.hashCode(BufferId(0)))
    assertEquals(0.hashCode(), IdentityId(0).hashCode())
    assertEquals(0.hashCode(), SignedIdProxy.hashCode(IdentityId(0)))
    assertEquals(0.hashCode(), MsgId(0).hashCode())
    assertEquals(0.hashCode(), SignedIdProxy.hashCode(MsgId(0)))
    assertEquals(0.hashCode(), NetworkId(0).hashCode())
    assertEquals(0.hashCode(), SignedIdProxy.hashCode(NetworkId(0)))
  }

  @Test
  fun testMinimal() {
    assertFalse(BufferId(Int.MIN_VALUE).isValid())
    assertFalse(SignedIdProxy.isValidId(BufferId(Int.MIN_VALUE)))
    assertFalse(IdentityId(Int.MIN_VALUE).isValid())
    assertFalse(SignedIdProxy.isValidId(IdentityId(Int.MIN_VALUE)))
    assertFalse(MsgId(Long.MIN_VALUE).isValid())
    assertFalse(SignedIdProxy.isValidId64(MsgId(Long.MIN_VALUE)))
    assertFalse(NetworkId(Int.MIN_VALUE).isValid())
    assertFalse(SignedIdProxy.isValidId(NetworkId(Int.MIN_VALUE)))

    assertEquals("BufferId(-2147483648)", BufferId(Int.MIN_VALUE).toString())
    assertEquals("BufferId(-2147483648)", SignedIdProxy.toString(BufferId(Int.MIN_VALUE)))
    assertEquals("IdentityId(-2147483648)", IdentityId(Int.MIN_VALUE).toString())
    assertEquals("IdentityId(-2147483648)", SignedIdProxy.toString(IdentityId(Int.MIN_VALUE)))
    assertEquals("MsgId(-9223372036854775808)", MsgId(Long.MIN_VALUE).toString())
    assertEquals("MsgId(-9223372036854775808)", SignedIdProxy.toString(MsgId(Long.MIN_VALUE)))
    assertEquals("NetworkId(-2147483648)", NetworkId(Int.MIN_VALUE).toString())
    assertEquals("NetworkId(-2147483648)", SignedIdProxy.toString(NetworkId(Int.MIN_VALUE)))

    assertEquals(Int.MIN_VALUE.hashCode(), BufferId(Int.MIN_VALUE).hashCode())
    assertEquals(Int.MIN_VALUE.hashCode(), SignedIdProxy.hashCode(BufferId(Int.MIN_VALUE)))
    assertEquals(Int.MIN_VALUE.hashCode(), IdentityId(Int.MIN_VALUE).hashCode())
    assertEquals(Int.MIN_VALUE.hashCode(), SignedIdProxy.hashCode(IdentityId(Int.MIN_VALUE)))
    assertEquals(Long.MIN_VALUE.hashCode(), MsgId(Long.MIN_VALUE).hashCode())
    assertEquals(Long.MIN_VALUE.hashCode(), SignedIdProxy.hashCode(MsgId(Long.MIN_VALUE)))
    assertEquals(Int.MIN_VALUE.hashCode(), NetworkId(Int.MIN_VALUE).hashCode())
    assertEquals(Int.MIN_VALUE.hashCode(), SignedIdProxy.hashCode(NetworkId(Int.MIN_VALUE)))
  }

  @Test
  fun testMaximum() {
    assertTrue(BufferId(Int.MAX_VALUE).isValid())
    assertTrue(SignedIdProxy.isValidId(BufferId(Int.MAX_VALUE)))
    assertTrue(IdentityId(Int.MAX_VALUE).isValid())
    assertTrue(SignedIdProxy.isValidId(IdentityId(Int.MAX_VALUE)))
    assertTrue(MsgId(Long.MAX_VALUE).isValid())
    assertTrue(SignedIdProxy.isValidId64(MsgId(Long.MAX_VALUE)))
    assertTrue(NetworkId(Int.MAX_VALUE).isValid())
    assertTrue(SignedIdProxy.isValidId(NetworkId(Int.MAX_VALUE)))

    assertEquals("BufferId(2147483647)", BufferId(Int.MAX_VALUE).toString())
    assertEquals("BufferId(2147483647)", SignedIdProxy.toString(BufferId(Int.MAX_VALUE)))
    assertEquals("IdentityId(2147483647)", IdentityId(Int.MAX_VALUE).toString())
    assertEquals("IdentityId(2147483647)", SignedIdProxy.toString(IdentityId(Int.MAX_VALUE)))
    assertEquals("MsgId(9223372036854775807)", MsgId(Long.MAX_VALUE).toString())
    assertEquals("MsgId(9223372036854775807)", SignedIdProxy.toString(MsgId(Long.MAX_VALUE)))
    assertEquals("NetworkId(2147483647)", NetworkId(Int.MAX_VALUE).toString())
    assertEquals("NetworkId(2147483647)", SignedIdProxy.toString(NetworkId(Int.MAX_VALUE)))

    assertEquals(Int.MAX_VALUE.hashCode(), BufferId(Int.MAX_VALUE).hashCode())
    assertEquals(Int.MAX_VALUE.hashCode(), SignedIdProxy.hashCode(BufferId(Int.MAX_VALUE)))
    assertEquals(Int.MAX_VALUE.hashCode(), IdentityId(Int.MAX_VALUE).hashCode())
    assertEquals(Int.MAX_VALUE.hashCode(), SignedIdProxy.hashCode(IdentityId(Int.MAX_VALUE)))
    assertEquals(Long.MAX_VALUE.hashCode(), MsgId(Long.MAX_VALUE).hashCode())
    assertEquals(Long.MAX_VALUE.hashCode(), SignedIdProxy.hashCode(MsgId(Long.MAX_VALUE)))
    assertEquals(Int.MAX_VALUE.hashCode(), NetworkId(Int.MAX_VALUE).hashCode())
    assertEquals(Int.MAX_VALUE.hashCode(), SignedIdProxy.hashCode(NetworkId(Int.MAX_VALUE)))
  }

  @Test
  fun testSortOrder() {
    assertEquals(
      listOf(
        BufferId(Int.MIN_VALUE),
        BufferId(-1),
        BufferId(0),
        BufferId(Int.MAX_VALUE)
      ),
      listOf(
        BufferId(Int.MAX_VALUE),
        BufferId(Int.MIN_VALUE),
        BufferId(0),
        BufferId(-1)
      ).sorted()
    )

    assertEquals(
      listOf(
        IdentityId(Int.MIN_VALUE),
        IdentityId(-1),
        IdentityId(0),
        IdentityId(Int.MAX_VALUE)
      ),
      listOf(
        IdentityId(Int.MAX_VALUE),
        IdentityId(Int.MIN_VALUE),
        IdentityId(0),
        IdentityId(-1)
      ).sorted()
    )

    assertEquals(
      listOf(
        MsgId(Long.MIN_VALUE),
        MsgId(-1),
        MsgId(0),
        MsgId(Long.MAX_VALUE)
      ),
      listOf(
        MsgId(Long.MAX_VALUE),
        MsgId(Long.MIN_VALUE),
        MsgId(0),
        MsgId(-1)
      ).sorted()
    )

    assertEquals(
      listOf(
        NetworkId(Int.MIN_VALUE),
        NetworkId(-1),
        NetworkId(0),
        NetworkId(Int.MAX_VALUE)
      ),
      listOf(
        NetworkId(Int.MAX_VALUE),
        NetworkId(Int.MIN_VALUE),
        NetworkId(0),
        NetworkId(-1)
      ).sorted()
    )
  }
}
