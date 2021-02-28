/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.HighlightNickType
import de.justjanne.libquassel.protocol.models.HighlightRule
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.HighlightRuleManagerState
import de.justjanne.libquassel.protocol.syncables.stubs.HighlightRuleManagerStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

open class HighlightRuleManager(
  session: Session? = null,
  state: HighlightRuleManagerState = HighlightRuleManagerState()
) : StatefulSyncableObject<HighlightRuleManagerState>(session, "HighlightRuleManager", state),
  HighlightRuleManagerStub {
  override fun fromVariantMap(properties: QVariantMap) {
    val highlightRules = properties["HighlightRuleList"].into<QVariantMap>().orEmpty()

    val idList = highlightRules["id"].into<QVariantList>().orEmpty()
    val nameList = highlightRules["name"].into<QStringList>().orEmpty()
    val isRegExList = highlightRules["isRegEx"].into<QVariantList>().orEmpty()
    val isCaseSensitiveList = highlightRules["isCaseSensitive"].into<QVariantList>().orEmpty()
    val isEnabledList = highlightRules["isEnabled"].into<QVariantList>().orEmpty()
    val isInverseList = highlightRules["isInverse"].into<QVariantList>().orEmpty()
    val senderList = highlightRules["sender"].into<QStringList>().orEmpty()
    val channelList = highlightRules["channel"].into<QStringList>().orEmpty()

    require(idList.size == nameList.size) {
      "Sizes do not match: ids=${idList.size}, nameList=${nameList.size}"
    }
    require(idList.size == isRegExList.size) {
      "Sizes do not match: ids=${idList.size}, isRegExList=${isRegExList.size}"
    }
    require(idList.size == isCaseSensitiveList.size) {
      "Sizes do not match: ids=${idList.size}, isCaseSensitiveList=${isCaseSensitiveList.size}"
    }
    require(idList.size == isEnabledList.size) {
      "Sizes do not match: ids=${idList.size}, isEnabledList=${isEnabledList.size}"
    }
    require(idList.size == isInverseList.size) {
      "Sizes do not match: ids=${idList.size}, isInverseList=${isInverseList.size}"
    }
    require(idList.size == senderList.size) {
      "Sizes do not match: ids=${idList.size}, senderList=${senderList.size}"
    }
    require(idList.size == channelList.size) {
      "Sizes do not match: ids=${idList.size}, channelList=${channelList.size}"
    }
    require(idList.size == channelList.size) {
      "Sizes do not match: ids=${idList.size}, channelList=${channelList.size}"
    }

    state.update {
      copy(
        highlightNickType = properties["highlightNick"].into<Int>()
          ?.let(HighlightNickType.Companion::of)
          ?: highlightNickType,
        highlightNickCaseSensitive = properties["nicksCaseSensitive"].into(highlightNickCaseSensitive),
        rules = List(idList.size) {
          HighlightRule(
            idList[it].into(0),
            nameList[it] ?: "",
            isRegEx = isRegExList[it].into(false),
            isCaseSensitive = isCaseSensitiveList[it].into(false),
            isEnabled = isEnabledList[it].into(false),
            isInverse = isInverseList[it].into(false),
            sender = senderList[it] ?: "",
            channel = channelList[it] ?: ""
          )
        }
      )
    }
  }

  override fun toVariantMap() = mapOf(
    "HighlightRuleList" to qVariant(
      mapOf(
        "id" to qVariant(
          state().rules.map { qVariant(it.id, QtType.Int) },
          QtType.QVariantList
        ),
        "name" to qVariant(
          state().rules.map(HighlightRule::contents),
          QtType.QStringList
        ),
        "isRegEx" to qVariant(
          state().rules.map { qVariant(it.isRegEx, QtType.Bool) },
          QtType.QVariantList
        ),
        "isCaseSensitive" to qVariant(
          state().rules.map { qVariant(it.isCaseSensitive, QtType.Bool) },
          QtType.QVariantList
        ),
        "isEnabled" to qVariant(
          state().rules.map { qVariant(it.isEnabled, QtType.Bool) },
          QtType.QVariantList
        ),
        "isInverse" to qVariant(
          state().rules.map { qVariant(it.isInverse, QtType.Bool) },
          QtType.QVariantList
        ),
        "sender" to qVariant(
          state().rules.map(HighlightRule::sender),
          QtType.QStringList
        ),
        "channel" to qVariant(
          state().rules.map(HighlightRule::channel),
          QtType.QStringList
        ),
      ),
      QtType.QVariantMap
    ),
    "highlightNick" to qVariant(state().highlightNickType.value, QtType.Int),
    "nicksCaseSensitive" to qVariant(state().highlightNickCaseSensitive, QtType.Bool)
  )

  fun indexOf(id: Int): Int = state().indexOf(id)
  fun contains(id: Int) = state().contains(id)

  fun isEmpty() = state().isEmpty()
  fun count() = state().count()
  fun removeAt(index: Int) {
    state.update {
      copy(rules = rules.drop(index))
    }
  }

  override fun removeHighlightRule(highlightRule: Int) {
    removeAt(indexOf(highlightRule))
    super.removeHighlightRule(highlightRule)
  }

  override fun toggleHighlightRule(highlightRule: Int) {
    state.update {
      copy(
        rules = rules.map {
          if (it.id != highlightRule) it
          else it.copy(isEnabled = !it.isEnabled)
        }
      )
    }
    super.toggleHighlightRule(highlightRule)
  }

  override fun addHighlightRule(
    id: Int,
    name: String?,
    isRegEx: Boolean,
    isCaseSensitive: Boolean,
    isEnabled: Boolean,
    isInverse: Boolean,
    sender: String?,
    chanName: String?
  ) {
    if (contains(id)) {
      return
    }

    state.update {
      copy(
        rules = rules + HighlightRule(
          id,
          name ?: "",
          isRegEx,
          isCaseSensitive,
          isEnabled,
          isInverse,
          sender ?: "",
          chanName ?: ""
        )
      )
    }

    super.addHighlightRule(id, name, isRegEx, isCaseSensitive, isEnabled, isInverse, sender, chanName)
  }

  override fun setHighlightNick(highlightNick: Int) {
    state.update {
      copy(highlightNickType = HighlightNickType.of(highlightNick) ?: highlightNickType)
    }
    super.setHighlightNick(highlightNick)
  }

  override fun setNicksCaseSensitive(nicksCaseSensitive: Boolean) {
    state.update {
      copy(highlightNickCaseSensitive = nicksCaseSensitive)
    }
    super.setNicksCaseSensitive(nicksCaseSensitive)
  }
}
