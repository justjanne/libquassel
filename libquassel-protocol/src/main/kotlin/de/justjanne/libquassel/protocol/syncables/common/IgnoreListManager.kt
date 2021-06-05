/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables.common

import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.flags.MessageTypes
import de.justjanne.libquassel.protocol.models.rules.IgnoreRule
import de.justjanne.libquassel.protocol.models.rules.IgnoreType
import de.justjanne.libquassel.protocol.models.rules.ScopeType
import de.justjanne.libquassel.protocol.models.rules.StrictnessType
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.session.Session
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableObject
import de.justjanne.libquassel.protocol.syncables.state.IgnoreListManagerState
import de.justjanne.libquassel.protocol.syncables.stubs.IgnoreListManagerStub
import de.justjanne.libquassel.protocol.util.collections.removeAt
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

class IgnoreListManager(
  session: Session? = null,
  state: IgnoreListManagerState = IgnoreListManagerState()
) : StatefulSyncableObject<IgnoreListManagerState>(session, "IgnoreListManager", state),
  IgnoreListManagerStub {
  override fun toVariantMap() = mapOf(
    "IgnoreList" to qVariant(
      mapOf(
        "ignoreType" to qVariant(
          state().rules.map {
            qVariant(it.type.value, QtType.Int)
          },
          QtType.QVariantList
        ),
        "ignoreRule" to qVariant(
          state().rules.map(IgnoreRule::ignoreRule),
          QtType.QStringList
        ),
        "isRegEx" to qVariant(
          state().rules.map {
            qVariant(it.isRegEx, QtType.Bool)
          },
          QtType.QVariantList
        ),
        "strictness" to qVariant(
          state().rules.map {
            qVariant(it.strictness.value, QtType.Int)
          },
          QtType.QVariantList
        ),
        "scope" to qVariant(
          state().rules.map {
            qVariant(it.scope.value, QtType.Int)
          },
          QtType.QVariantList
        ),
        "isActive" to qVariant(
          state().rules.map {
            qVariant(it.isEnabled, QtType.Bool)
          },
          QtType.QVariantList
        ),
        "scopeRule" to qVariant(
          state().rules.map(IgnoreRule::scopeRule),
          QtType.QStringList
        ),
      ),
      QtType.QVariantMap
    )
  )

  override fun fromVariantMap(properties: QVariantMap) {
    val ignoreRules = properties["IgnoreList"].into<QVariantMap>().orEmpty()

    val ignoreTypeList = ignoreRules["ignoreType"].into<QVariantList>().orEmpty()
    val ignoreRuleList = ignoreRules["ignoreRule"].into<QStringList>().orEmpty()
    val isRegExList = ignoreRules["isRegEx"].into<QVariantList>().orEmpty()
    val strictnessList = ignoreRules["strictness"].into<QVariantList>().orEmpty()
    val isEnabledList = ignoreRules["isActive"].into<QVariantList>().orEmpty()
    val scopeList = ignoreRules["scope"].into<QVariantList>().orEmpty()
    val scopeRuleList = ignoreRules["scopeRule"].into<QStringList>().orEmpty()

    require(ignoreTypeList.size == ignoreRuleList.size) {
      "Sizes do not match: ids=${ignoreTypeList.size}, ignoreRule=${ignoreRuleList.size}"
    }
    require(ignoreTypeList.size == isRegExList.size) {
      "Sizes do not match: ids=${ignoreTypeList.size}, isRegExList=${isRegExList.size}"
    }
    require(ignoreTypeList.size == strictnessList.size) {
      "Sizes do not match: ids=${ignoreTypeList.size}, strictnessList=${strictnessList.size}"
    }
    require(ignoreTypeList.size == isEnabledList.size) {
      "Sizes do not match: ids=${ignoreTypeList.size}, isEnabledList=${isEnabledList.size}"
    }
    require(ignoreTypeList.size == scopeList.size) {
      "Sizes do not match: ids=${ignoreTypeList.size}, scopeList=${scopeList.size}"
    }
    require(ignoreTypeList.size == scopeRuleList.size) {
      "Sizes do not match: ids=${ignoreTypeList.size}, scopeRuleList=${scopeRuleList.size}"
    }

    state.update {
      copy(
        rules = List(ignoreTypeList.size) {
          IgnoreRule(
            type = ignoreTypeList[it].into<Int>()?.let(IgnoreType::of)
              ?: IgnoreType.SenderIgnore,
            ignoreRule = ignoreRuleList[it] ?: "",
            isRegEx = isRegExList[it].into(false),
            strictness = strictnessList[it].into<Int>()?.let(StrictnessType::of)
              ?: StrictnessType.UnmatchedStrictness,
            isEnabled = isEnabledList[it].into(false),
            scope = scopeList[it].into<Int>()?.let(ScopeType::of)
              ?: ScopeType.GlobalScope,
            scopeRule = scopeRuleList[it] ?: "",
          )
        }
      )
    }
    initialized = true
  }

  fun indexOf(ignoreRule: String?): Int = state().indexOf(ignoreRule)
  fun contains(ignoreRule: String?) = state().contains(ignoreRule)

  fun isEmpty() = state().isEmpty()
  fun count() = state().count()
  fun removeAt(index: Int) {
    state.update {
      copy(rules = rules.removeAt(index))
    }
  }

  override fun addIgnoreListItem(
    type: Int,
    ignoreRule: String?,
    isRegEx: Boolean,
    strictness: Int,
    scope: Int,
    scopeRule: String?,
    isActive: Boolean
  ) {
    if (contains(ignoreRule)) {
      return
    }

    state.update {
      copy(
        rules = rules + IgnoreRule(
          type = IgnoreType.of(type) ?: return,
          ignoreRule = ignoreRule ?: "",
          isRegEx = isRegEx,
          strictness = StrictnessType.of(strictness) ?: return,
          scope = ScopeType.of(scope) ?: return,
          scopeRule = scopeRule ?: "",
          isEnabled = isActive
        )
      )
    }

    super.addIgnoreListItem(type, ignoreRule, isRegEx, strictness, scope, scopeRule, isActive)
  }

  override fun removeIgnoreListItem(ignoreRule: String?) {
    removeAt(indexOf(ignoreRule))

    super.removeIgnoreListItem(ignoreRule)
  }

  override fun toggleIgnoreRule(ignoreRule: String?) {
    state.update {
      copy(
        rules = rules.map {
          if (it.ignoreRule != ignoreRule) it
          else it.copy(isEnabled = !it.isEnabled)
        }
      )
    }

    super.toggleIgnoreRule(ignoreRule)
  }

  fun match(
    msgContents: String,
    msgSender: String,
    msgType: MessageTypes,
    network: String,
    bufferName: String
  ) = state().match(msgContents, msgSender, msgType, network, bufferName)
}
