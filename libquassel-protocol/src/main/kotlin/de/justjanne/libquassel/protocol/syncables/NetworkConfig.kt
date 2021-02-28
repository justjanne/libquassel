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

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.syncables.state.NetworkConfigState
import de.justjanne.libquassel.protocol.syncables.stubs.NetworkConfigStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

open class NetworkConfig(
  session: Session? = null,
  state: NetworkConfigState = NetworkConfigState()
) : StatefulSyncableObject<NetworkConfigState>(session, "NetworkConfig", state),
  NetworkConfigStub {
  init {
    renameObject("GlobalNetworkConfig")
  }

  override fun fromVariantMap(properties: QVariantMap) {
    state.update {
      copy(
        pingTimeoutEnabled = properties["pingTimeoutEnabled"].into(pingTimeoutEnabled),
        pingInterval = properties["pingInterval"].into(pingInterval),
        maxPingCount = properties["maxPingCount"].into(maxPingCount),
        autoWhoEnabled = properties["autoWhoEnabled"].into(autoWhoEnabled),
        autoWhoInterval = properties["autoWhoInterval"].into(autoWhoInterval),
        autoWhoNickLimit = properties["autoWhoNickLimit"].into(autoWhoNickLimit),
        autoWhoDelay = properties["autoWhoDelay"].into(autoWhoDelay),
        standardCtcp = properties["standardCtcp"].into(standardCtcp),
      )
    }
  }

  override fun toVariantMap() = mapOf(
    "pingTimeoutEnabled" to qVariant(pingTimeoutEnabled(), QtType.Bool),
    "pingInterval" to qVariant(pingInterval(), QtType.Int),
    "maxPingCount" to qVariant(maxPingCount(), QtType.Int),
    "autoWhoEnabled" to qVariant(autoWhoEnabled(), QtType.Bool),
    "autoWhoInterval" to qVariant(autoWhoInterval(), QtType.Int),
    "autoWhoNickLimit" to qVariant(autoWhoNickLimit(), QtType.Int),
    "autoWhoDelay" to qVariant(autoWhoDelay(), QtType.Int),
    "standardCtcp" to qVariant(standardCtcp(), QtType.Bool)
  )

  fun pingTimeoutEnabled() = state().pingTimeoutEnabled
  fun pingInterval() = state().pingInterval
  fun maxPingCount() = state().maxPingCount
  fun autoWhoEnabled() = state().autoWhoEnabled
  fun autoWhoInterval() = state().autoWhoInterval
  fun autoWhoNickLimit() = state().autoWhoNickLimit
  fun autoWhoDelay() = state().autoWhoDelay
  fun standardCtcp() = state().standardCtcp

  override fun setAutoWhoDelay(delay: Int) {
    state.update {
      copy(autoWhoDelay = delay)
    }
    super.setAutoWhoDelay(delay)
  }

  override fun setAutoWhoEnabled(enabled: Boolean) {
    state.update {
      copy(autoWhoEnabled = enabled)
    }
    super.setAutoWhoEnabled(enabled)
  }

  override fun setAutoWhoInterval(interval: Int) {
    state.update {
      copy(autoWhoInterval = interval)
    }
    super.setAutoWhoInterval(interval)
  }

  override fun setAutoWhoNickLimit(limit: Int) {
    state.update {
      copy(autoWhoNickLimit = limit)
    }
    super.setAutoWhoNickLimit(limit)
  }

  override fun setMaxPingCount(count: Int) {
    state.update {
      copy(maxPingCount = count)
    }
    super.setMaxPingCount(count)
  }

  override fun setPingInterval(interval: Int) {
    state.update {
      copy(pingInterval = interval)
    }
    super.setPingInterval(interval)
  }

  override fun setPingTimeoutEnabled(enabled: Boolean) {
    state.update {
      copy(pingTimeoutEnabled = enabled)
    }
    super.setPingTimeoutEnabled(enabled)
  }

  override fun setStandardCtcp(enabled: Boolean) {
    state.update {
      copy(standardCtcp = enabled)
    }
    super.setStandardCtcp(enabled)
  }
}
