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
import de.justjanne.libquassel.protocol.models.ids.IdentityId
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.Session
import de.justjanne.libquassel.protocol.syncables.StatefulSyncableObject
import de.justjanne.libquassel.protocol.syncables.state.IdentityState
import de.justjanne.libquassel.protocol.syncables.stubs.IdentityStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

open class Identity(
  session: Session? = null,
  state: IdentityState = IdentityState()
) : StatefulSyncableObject<IdentityState>(session, "Identity", state),
  IdentityStub {
  init {
    renameObject(state().identifier())
  }

  override fun fromVariantMap(properties: QVariantMap) {
    state.update {
      copy(
        identityId = properties["identityId"].into(identityId),
        identityName = properties["identityName"].into(identityName),
        realName = properties["realName"].into(realName),
        nicks = properties["nicks"].into(nicks),
        awayNick = properties["awayNick"].into(awayNick),
        awayNickEnabled = properties["awayNickEnabled"].into(awayNickEnabled),
        awayReason = properties["awayReason"].into(awayReason),
        awayReasonEnabled = properties["awayReasonEnabled"].into(awayReasonEnabled),
        autoAwayEnabled = properties["autoAwayEnabled"].into(autoAwayEnabled),
        autoAwayTime = properties["autoAwayTime"].into(autoAwayTime),
        autoAwayReason = properties["autoAwayReason"].into(autoAwayReason),
        autoAwayReasonEnabled = properties["autoAwayReasonEnabled"].into(autoAwayReasonEnabled),
        detachAwayEnabled = properties["detachAwayEnabled"].into(detachAwayEnabled),
        detachAwayReason = properties["detachAwayReason"].into(detachAwayReason),
        detachAwayReasonEnabled = properties["detachAwayReasonEnabled"].into(detachAwayReasonEnabled),
        ident = properties["ident"].into(ident),
        kickReason = properties["kickReason"].into(kickReason),
        partReason = properties["partReason"].into(partReason),
        quitReason = properties["quitReason"].into(quitReason),
      )
    }
    renameObject(state().identifier())
    initialized = true
  }

  override fun toVariantMap() = mapOf(
    "identityId" to qVariant(id(), QuasselType.IdentityId),
    "identityName" to qVariant(identityName(), QtType.QString),
    "realName" to qVariant(realName(), QtType.QString),
    "nicks" to qVariant(nicks(), QtType.QStringList),
    "awayNick" to qVariant(awayNick(), QtType.QString),
    "awayNickEnabled" to qVariant(awayNickEnabled(), QtType.Bool),
    "awayReason" to qVariant(awayReason(), QtType.QString),
    "awayReasonEnabled" to qVariant(awayReasonEnabled(), QtType.Bool),
    "autoAwayEnabled" to qVariant(autoAwayEnabled(), QtType.Bool),
    "autoAwayTime" to qVariant(autoAwayTime(), QtType.Int),
    "autoAwayReason" to qVariant(autoAwayReason(), QtType.QString),
    "autoAwayReasonEnabled" to qVariant(autoAwayReasonEnabled(), QtType.Bool),
    "detachAwayEnabled" to qVariant(detachAwayEnabled(), QtType.Bool),
    "detachAwayReason" to qVariant(detachAwayReason(), QtType.QString),
    "detachAwayReasonEnabled" to qVariant(detachAwayReasonEnabled(), QtType.Bool),
    "ident" to qVariant(ident(), QtType.QString),
    "kickReason" to qVariant(kickReason(), QtType.QString),
    "partReason" to qVariant(partReason(), QtType.QString),
    "quitReason" to qVariant(quitReason(), QtType.QString)
  )

  fun id() = state().identityId
  fun identityName() = state().identityName
  fun realName() = state().realName
  fun nicks() = state().nicks
  fun awayNick() = state().awayNick
  fun awayNickEnabled() = state().awayNickEnabled
  fun awayReason() = state().awayReason
  fun awayReasonEnabled() = state().awayReasonEnabled
  fun autoAwayEnabled() = state().autoAwayEnabled
  fun autoAwayTime() = state().autoAwayTime
  fun autoAwayReason() = state().autoAwayReason
  fun autoAwayReasonEnabled() = state().autoAwayReasonEnabled
  fun detachAwayEnabled() = state().detachAwayEnabled
  fun detachAwayReason() = state().detachAwayReason
  fun detachAwayReasonEnabled() = state().detachAwayReasonEnabled
  fun ident() = state().ident
  fun kickReason() = state().kickReason
  fun partReason() = state().partReason
  fun quitReason() = state().quitReason

  override fun setAutoAwayEnabled(enabled: Boolean) {
    state.update {
      copy(autoAwayEnabled = enabled)
    }
    super.setAutoAwayEnabled(enabled)
  }

  override fun setAutoAwayReason(reason: String?) {
    state.update {
      copy(autoAwayReason = reason ?: "")
    }
    super.setAutoAwayReason(reason)
  }

  override fun setAutoAwayReasonEnabled(enabled: Boolean) {
    state.update {
      copy(autoAwayReasonEnabled = enabled)
    }
    super.setAutoAwayReasonEnabled(enabled)
  }

  override fun setAutoAwayTime(time: Int) {
    state.update {
      copy(autoAwayTime = time)
    }
    super.setAutoAwayTime(time)
  }

  override fun setAwayNick(awayNick: String?) {
    state.update {
      copy(awayNick = awayNick ?: "")
    }
    super.setAwayNick(awayNick)
  }

  override fun setAwayNickEnabled(enabled: Boolean) {
    state.update {
      copy(awayNickEnabled = enabled)
    }
    super.setAwayNickEnabled(enabled)
  }

  override fun setAwayReason(awayReason: String?) {
    state.update {
      copy(awayReason = awayReason ?: "")
    }
    super.setAwayReason(awayReason)
  }

  override fun setAwayReasonEnabled(enabled: Boolean) {
    state.update {
      copy(awayReasonEnabled = enabled)
    }
    super.setAwayReasonEnabled(enabled)
  }

  override fun setDetachAwayEnabled(enabled: Boolean) {
    state.update {
      copy(detachAwayEnabled = enabled)
    }
    super.setDetachAwayEnabled(enabled)
  }

  override fun setDetachAwayReason(reason: String?) {
    state.update {
      copy(detachAwayReason = reason ?: "")
    }
    super.setDetachAwayReason(reason)
  }

  override fun setDetachAwayReasonEnabled(enabled: Boolean) {
    state.update {
      copy(detachAwayReasonEnabled = enabled)
    }
    super.setDetachAwayReasonEnabled(enabled)
  }

  override fun setId(id: IdentityId) {
    state.update {
      copy(identityId = id)
    }
    super.setId(id)
  }

  override fun setIdent(ident: String?) {
    state.update {
      copy(ident = ident ?: "")
    }
    super.setIdent(ident)
  }

  override fun setIdentityName(name: String?) {
    state.update {
      copy(identityName = name ?: "")
    }
    super.setIdentityName(name)
  }

  override fun setKickReason(reason: String?) {
    state.update {
      copy(kickReason = reason ?: "")
    }
    super.setKickReason(reason)
  }

  override fun setNicks(nicks: QStringList) {
    state.update {
      copy(nicks = nicks.map { it ?: "" })
    }
    super.setNicks(nicks)
  }

  override fun setPartReason(reason: String?) {
    state.update {
      copy(partReason = reason ?: "")
    }
    super.setPartReason(reason)
  }

  override fun setQuitReason(reason: String?) {
    state.update {
      copy(quitReason = reason ?: "")
    }
    super.setQuitReason(reason)
  }

  override fun setRealName(realName: String?) {
    state.update {
      copy(realName = realName ?: "")
    }
    super.setRealName(realName)
  }
}
