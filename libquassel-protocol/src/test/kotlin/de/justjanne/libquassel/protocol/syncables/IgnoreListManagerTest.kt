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
import de.justjanne.libquassel.protocol.models.rules.IgnoreRule
import de.justjanne.libquassel.protocol.models.rules.IgnoreType
import de.justjanne.libquassel.protocol.models.rules.ScopeType
import de.justjanne.libquassel.protocol.models.rules.StrictnessType
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.common.IgnoreListManager
import de.justjanne.libquassel.protocol.syncables.state.IgnoreListManagerState
import de.justjanne.libquassel.protocol.testutil.nextIgnoreRule
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

class IgnoreListManagerTest {
  @Test
  fun testEmpty() {
    val actual = IgnoreListManager().apply {
      update(emptyMap())
    }.state()

    assertEquals(IgnoreListManagerState(), actual)
  }

  @Test
  fun testInvalidData() {
    val state = IgnoreListManagerState()
    val actual = IgnoreListManager(state = state).apply {
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "IgnoreList" to qVariant<QVariantMap>(
              mapOf(
                "ignoreType" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "ignoreRule" to qVariant<QStringList>(listOf(""), QtType.QStringList),
              ),
              QtType.QVariantMap
            )
          )
        )
      }
      assertThrows<IllegalArgumentException> {
        update(
          mapOf(
            "IgnoreList" to qVariant<QVariantMap>(
              mapOf(
                "ignoreType" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
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
            "IgnoreList" to qVariant<QVariantMap>(
              mapOf(
                "ignoreType" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "strictness" to qVariant<QVariantList>(
                  listOf(
                    qVariant(StrictnessType.SoftStrictness.value, QtType.Int)
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
            "IgnoreList" to qVariant<QVariantMap>(
              mapOf(
                "ignoreType" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "isActive" to qVariant<QVariantList>(
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
            "IgnoreList" to qVariant<QVariantMap>(
              mapOf(
                "ignoreType" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "scope" to qVariant<QVariantList>(
                  listOf(
                    qVariant(ScopeType.GlobalScope.value, QtType.Int)
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
            "IgnoreList" to qVariant<QVariantMap>(
              mapOf(
                "ignoreType" to qVariant<QVariantList>(emptyList(), QtType.QVariantList),
                "scopeRule" to qVariant<QStringList>(listOf(""), QtType.QStringList),
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
  fun testNulLData() {
    val actual = IgnoreListManager(
      state = IgnoreListManagerState()
    ).apply {
      update(
        mapOf(
          "IgnoreList" to qVariant(
            mapOf(
              "ignoreType" to qVariant(
                listOf(
                  qVariant(-2, QtType.Int)
                ),
                QtType.QVariantList
              ),
              "ignoreRule" to qVariant(
                listOf(null),
                QtType.QStringList
              ),
              "isRegEx" to qVariant(
                listOf(
                  qVariant(false, QtType.Bool)
                ),
                QtType.QVariantList
              ),
              "strictness" to qVariant(
                listOf(
                  qVariant(-2, QtType.Int)
                ),
                QtType.QVariantList
              ),
              "isActive" to qVariant(
                listOf(
                  qVariant(false, QtType.Bool)
                ),
                QtType.QVariantList
              ),
              "scope" to qVariant(
                listOf(
                  qVariant(-2, QtType.Int)
                ),
                QtType.QVariantList
              ),
              "scopeRule" to qVariant(
                listOf(
                  null
                ),
                QtType.QStringList
              )
            ),
            QtType.QVariantMap
          )
        )
      )
    }.state()

    assertEquals(
      IgnoreRule(
        type = IgnoreType.SenderIgnore,
        ignoreRule = "",
        isRegEx = false,
        strictness = StrictnessType.UnmatchedStrictness,
        isEnabled = false,
        scope = ScopeType.GlobalScope,
        scopeRule = ""
      ),
      actual.rules.first()
    )
  }

  @Test
  fun testSerialization() {
    val random = Random(1337)
    val expected = IgnoreListManagerState(
      rules = List(random.nextInt(20)) {
        random.nextIgnoreRule()
      }
    )

    val actual = IgnoreListManager(
      state = IgnoreListManagerState()
    ).apply {
      update(IgnoreListManager(state = expected).toVariantMap())
    }.state()

    assertEquals(expected, actual)
  }

  @Nested
  inner class Setters {
    @Test
    fun testRemoveIgnoreRule() {
      val random = Random(1337)
      val value = IgnoreListManager(
        state = IgnoreListManagerState(
          rules = List(random.nextInt(20)) {
            random.nextIgnoreRule()
          }
        )
      )

      val rule = value.state().rules.random(random)
      assertTrue(value.contains(rule.ignoreRule))
      assertNotEquals(-1, value.indexOf(rule.ignoreRule))
      value.removeIgnoreListItem(rule.ignoreRule)
      assertFalse(value.contains(rule.ignoreRule))
      assertEquals(-1, value.indexOf(rule.ignoreRule))
    }

    @Test
    fun testRemoveAll() {
      val random = Random(1337)
      val value = IgnoreListManager(
        state = IgnoreListManagerState(
          rules = List(random.nextInt(20)) {
            random.nextIgnoreRule()
          }
        )
      )

      assertFalse(value.isEmpty())
      for (rule in value.state().rules) {
        println(rule)
        val index = value.state().indexOf(rule.ignoreRule)
        println(index)
        println(value.state().rules[index])
        value.removeIgnoreListItem(rule.ignoreRule)
      }
      assertTrue(value.isEmpty())
      assertEquals(0, value.count())
    }

    @Test
    fun testToggleHighlightRule() {
      val random = Random(1337)
      val value = IgnoreListManager(
        state = IgnoreListManagerState(
          rules = List(random.nextInt(20)) {
            random.nextIgnoreRule()
          }
        )
      )

      val rule = value.state().rules.random(random)
      assertTrue(value.contains(rule.ignoreRule))
      assertNotEquals(-1, value.indexOf(rule.ignoreRule))
      assertTrue(value.state().rules[value.indexOf(rule.ignoreRule)].isEnabled)
      value.toggleIgnoreRule(rule.ignoreRule)
      assertTrue(value.contains(rule.ignoreRule))
      assertNotEquals(-1, value.indexOf(rule.ignoreRule))
      assertFalse(value.state().rules[value.indexOf(rule.ignoreRule)].isEnabled)
      value.toggleIgnoreRule(rule.ignoreRule)
      assertTrue(value.contains(rule.ignoreRule))
      assertNotEquals(-1, value.indexOf(rule.ignoreRule))
      assertTrue(value.state().rules[value.indexOf(rule.ignoreRule)].isEnabled)
    }

    @Test
    fun testAddExisting() {
      val random = Random(1337)
      val value = IgnoreListManager(
        state = IgnoreListManagerState(
          rules = List(random.nextInt(20)) {
            random.nextIgnoreRule()
          }
        )
      )

      val rule = value.state().rules.random(random)
      val sizeBefore = value.count()
      value.addIgnoreListItem(
        type = rule.type.value,
        ignoreRule = rule.ignoreRule,
        isRegEx = rule.isRegEx,
        strictness = rule.strictness.value,
        scope = rule.scope.value,
        scopeRule = rule.scopeRule,
        isActive = rule.isEnabled
      )
      assertEquals(sizeBefore, value.count())
    }

    @Test
    fun testAddNew() {
      val random = Random(1337)
      val value = IgnoreListManager(
        state = IgnoreListManagerState(
          rules = List(random.nextInt(20)) {
            random.nextIgnoreRule()
          }
        )
      )

      val rule = random.nextIgnoreRule()
      val sizeBefore = value.count()
      value.addIgnoreListItem(
        type = rule.type.value,
        ignoreRule = rule.ignoreRule,
        isRegEx = rule.isRegEx,
        strictness = rule.strictness.value,
        scope = rule.scope.value,
        scopeRule = rule.scopeRule,
        isActive = rule.isEnabled
      )
      assertEquals(sizeBefore + 1, value.count())
    }

    @Test
    fun testAddEdgeCase() {
      val random = Random(1337)
      val value = IgnoreListManager(
        state = IgnoreListManagerState(
          rules = List(random.nextInt(20)) {
            random.nextIgnoreRule()
          }
        )
      )

      val rule = random.nextIgnoreRule()
      val sizeBefore = value.count()
      value.addIgnoreListItem(
        type = rule.type.value,
        ignoreRule = null,
        isRegEx = rule.isRegEx,
        strictness = rule.strictness.value,
        scope = rule.scope.value,
        scopeRule = null,
        isActive = rule.isEnabled
      )
      assertEquals(sizeBefore + 1, value.count())
    }

    @Test
    fun testAddEdgeCaseUnchanged() {
      val random = Random(1337)
      val value = IgnoreListManager(
        state = IgnoreListManagerState(
          rules = List(random.nextInt(20)) {
            random.nextIgnoreRule()
          }
        )
      )

      val rule = random.nextIgnoreRule()
      val sizeBefore = value.count()
      value.addIgnoreListItem(
        type = -2,
        ignoreRule = null,
        isRegEx = rule.isRegEx,
        strictness = rule.strictness.value,
        scope = rule.scope.value,
        scopeRule = null,
        isActive = rule.isEnabled
      )
      assertEquals(sizeBefore, value.count())
      value.addIgnoreListItem(
        type = rule.type.value,
        ignoreRule = null,
        isRegEx = rule.isRegEx,
        strictness = -2,
        scope = rule.scope.value,
        scopeRule = null,
        isActive = rule.isEnabled
      )
      assertEquals(sizeBefore, value.count())
      value.addIgnoreListItem(
        type = rule.type.value,
        ignoreRule = null,
        isRegEx = rule.isRegEx,
        strictness = rule.strictness.value,
        scope = -2,
        scopeRule = null,
        isActive = rule.isEnabled
      )
      assertEquals(sizeBefore, value.count())
    }
  }
}
