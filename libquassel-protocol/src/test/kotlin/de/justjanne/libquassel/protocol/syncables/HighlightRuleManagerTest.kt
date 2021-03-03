/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.rules.HighlightNickType
import de.justjanne.libquassel.protocol.models.rules.HighlightRule
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.HighlightRuleManagerState
import de.justjanne.libquassel.protocol.testutil.nextEnum
import de.justjanne.libquassel.protocol.testutil.nextHighlightRule
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.qVariant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class HighlightRuleManagerTest {
  @Test
  fun testEmpty() {
    val actual = HighlightRuleManager().apply {
      update(emptyMap())
    }.state()

    assertEquals(HighlightRuleManagerState(), actual)
  }

  @Test
  fun testInvalidData() {
    val state = HighlightRuleManagerState()
    val actual = HighlightRuleManager(state = state).apply {
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "HighlightRuleList" to qVariant<QVariantMap>(
              mapOf(
                "id" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "name" to qVariant<QStringList>(listOf(""), QtType.QStringList),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "HighlightRuleList" to qVariant<QVariantMap>(
              mapOf(
                "id" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "isRegEx" to qVariant<QVariantList>(
                  listOf(
                    qVariant(false, QtType.Bool)
                  ),
                  QtType.QVariantList
                ),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "HighlightRuleList" to qVariant<QVariantMap>(
              mapOf(
                "id" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "isCaseSensitive" to qVariant<QVariantList>(
                  listOf(
                    qVariant(false, QtType.Bool)
                  ),
                  QtType.QVariantList
                ),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "HighlightRuleList" to qVariant<QVariantMap>(
              mapOf(
                "id" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "isEnabled" to qVariant<QVariantList>(
                  listOf(
                    qVariant(false, QtType.Bool)
                  ),
                  QtType.QVariantList
                ),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "HighlightRuleList" to qVariant<QVariantMap>(
              mapOf(
                "id" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "isInverse" to qVariant<QVariantList>(
                  listOf(
                    qVariant(false, QtType.Bool)
                  ),
                  QtType.QVariantList
                ),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "HighlightRuleList" to qVariant<QVariantMap>(
              mapOf(
                "id" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "sender" to qVariant<QStringList>(listOf(""), QtType.QStringList),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "HighlightRuleList" to qVariant<QVariantMap>(
              mapOf(
                "id" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "channel" to qVariant<QStringList>(listOf(""), QtType.QStringList),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
    }.state()

    assertEquals(state, actual)
  }

  @Test
  fun testNullData() {
    val actual = HighlightRuleManager(
      state = HighlightRuleManagerState()
    ).apply {
      update(
        mapOf(
          "HighlightRuleList" to qVariant(
            mapOf(
              "id" to qVariant(
                listOf(
                  qVariant(999, QtType.Int)
                ),
                QtType.QVariantList
              ),
              "name" to qVariant(
                listOf(
                  null
                ),
                QtType.QStringList
              ),
              "isRegEx" to qVariant(
                listOf(
                  qVariant(false, QtType.Bool)
                ),
                QtType.QVariantList
              ),
              "isCaseSensitive" to qVariant(
                listOf(
                  qVariant(false, QtType.Bool)
                ),
                QtType.QVariantList
              ),
              "isEnabled" to qVariant(
                listOf(
                  qVariant(false, QtType.Bool)
                ),
                QtType.QVariantList
              ),
              "isInverse" to qVariant(
                listOf(
                  qVariant(false, QtType.Bool)
                ),
                QtType.QVariantList
              ),
              "sender" to qVariant(
                listOf(
                  null
                ),
                QtType.QStringList
              ),
              "channel" to qVariant(
                listOf(
                  null
                ),
                QtType.QStringList
              )
            ),
            QtType.QVariantMap
          ),
          "highlightNick" to qVariant(-2, QtType.Int)
        )
      )
    }.state()

    assertEquals(
      HighlightRule(
        id = 999,
        content = "",
        isRegEx = false,
        isCaseSensitive = false,
        isEnabled = false,
        isInverse = false,
        sender = "",
        channel = ""
      ),
      actual.rules.first()
    )
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = HighlightRuleManagerState(
      rules = List(random.nextInt(20)) {
        random.nextHighlightRule(it)
      },
      highlightNickType = random.nextEnum(),
      highlightNickCaseSensitive = random.nextBoolean()
    )

    val actual = HighlightRuleManager(
      state = HighlightRuleManagerState()
    ).apply {
      update(HighlightRuleManager(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class Setters {
    @Test
    fun testRemoveHighlightRule() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      val rule = value.state().rules.random(random)
      assertTrue(value.contains(rule.id))
      assertNotEquals(-1, value.indexOf(rule.id))
      value.removeHighlightRule(rule.id)
      assertFalse(value.contains(rule.id))
      assertEquals(-1, value.indexOf(rule.id))
    }

    @Test
    fun testRemoveAll() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      assertFalse(value.isEmpty())
      for (rule in value.state().rules) {
        value.removeHighlightRule(rule.id)
      }
      assertTrue(value.isEmpty())
      assertEquals(0, value.count())
    }

    @Test
    fun testToggleHighlightRule() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      val rule = value.state().rules.random(random)
      assertTrue(value.contains(rule.id))
      assertNotEquals(-1, value.indexOf(rule.id))
      assertFalse(value.state().rules[value.indexOf(rule.id)].isEnabled)
      value.toggleHighlightRule(rule.id)
      assertTrue(value.contains(rule.id))
      assertNotEquals(-1, value.indexOf(rule.id))
      assertTrue(value.state().rules[value.indexOf(rule.id)].isEnabled)
      value.toggleHighlightRule(rule.id)
      assertTrue(value.contains(rule.id))
      assertNotEquals(-1, value.indexOf(rule.id))
      assertFalse(value.state().rules[value.indexOf(rule.id)].isEnabled)
    }

    @Test
    fun testHighlightNick() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      assertEquals(HighlightNickType.AllNicks, value.state().highlightNickType)
      value.setHighlightNick(HighlightNickType.CurrentNick.value)
      assertEquals(HighlightNickType.CurrentNick, value.state().highlightNickType)
      value.setHighlightNick(-2)
      assertEquals(HighlightNickType.CurrentNick, value.state().highlightNickType)
    }

    @Test
    fun testNicksCaseSensitive() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      value.setNicksCaseSensitive(false)
      assertEquals(false, value.state().highlightNickCaseSensitive)
      value.setNicksCaseSensitive(true)
      assertEquals(true, value.state().highlightNickCaseSensitive)
    }

    @Test
    fun testAddExisting() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      val rule = value.state().rules.random(random)
      val sizeBefore = value.count()
      value.addHighlightRule(
        id = rule.id,
        content = rule.content,
        isRegEx = rule.isRegEx,
        isCaseSensitive = rule.isCaseSensitive,
        isEnabled = rule.isEnabled,
        isInverse = rule.isInverse,
        sender = rule.sender,
        channel = rule.channel
      )
      assertEquals(sizeBefore, value.count())
    }

    @Test
    fun testAddNew() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      val rule = random.nextHighlightRule(value.count())
      val sizeBefore = value.count()
      value.addHighlightRule(
        id = rule.id,
        content = rule.content,
        isRegEx = rule.isRegEx,
        isCaseSensitive = rule.isCaseSensitive,
        isEnabled = rule.isEnabled,
        isInverse = rule.isInverse,
        sender = rule.sender,
        channel = rule.channel
      )
      assertEquals(sizeBefore + 1, value.count())
    }

    @Test
    fun testAddEdgeCase() {
      val random = Random(1337)
      val value = HighlightRuleManager(
        state = HighlightRuleManagerState(
          rules = List(random.nextInt(20)) {
            random.nextHighlightRule(it)
          },
          highlightNickType = random.nextEnum(),
          highlightNickCaseSensitive = random.nextBoolean()
        )
      )

      val rule = random.nextHighlightRule(value.count())
      val sizeBefore = value.count()
      value.addHighlightRule(
        id = rule.id,
        content = null,
        isRegEx = rule.isRegEx,
        isCaseSensitive = rule.isCaseSensitive,
        isEnabled = rule.isEnabled,
        isInverse = rule.isInverse,
        sender = null,
        channel = null
      )
      assertEquals(sizeBefore + 1, value.count())
    }
  }
}
