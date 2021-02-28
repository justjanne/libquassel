/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.syncables

import de.justjanne.libquassel.protocol.models.dcc.DccIpDetectionMode
import de.justjanne.libquassel.protocol.models.dcc.DccPortSelectionMode
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType
import de.justjanne.libquassel.protocol.syncables.state.DccConfigState
import de.justjanne.libquassel.protocol.syncables.stubs.DccConfigStub
import de.justjanne.libquassel.protocol.util.update
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant
import java.net.InetAddress

open class DccConfig(
  session: Session? = null,
  state: DccConfigState = DccConfigState()
) : StatefulSyncableObject<DccConfigState>(session, "DccConfig", state),
  DccConfigStub {
  init {
    renameObject("DccConfig")
  }

  override fun fromVariantMap(properties: QVariantMap) {
    state.update {
      copy(
        dccEnabled = properties["dccEnabled"].into(dccEnabled),
        outgoingIp = properties["outgoingIp"].into(outgoingIp),
        ipDetectionMode = properties["ipDetectionMode"].into(ipDetectionMode),
        portSelectionMode = properties["portSelectionMode"].into(portSelectionMode),
        minPort = properties["minPort"].into(minPort),
        maxPort = properties["maxPort"].into(maxPort),
        chunkSize = properties["chunkSize"].into(chunkSize),
        sendTimeout = properties["sendTimeout"].into(sendTimeout),
        usePassiveDcc = properties["usePassiveDcc"].into(usePassiveDcc),
        useFastSend = properties["useFastSend"].into(useFastSend),
      )
    }
  }

  override fun toVariantMap() = mapOf(
    "dccEnabled" to qVariant(state().dccEnabled, QtType.Bool),
    "outgoingIp" to qVariant(state().outgoingIp, QuasselType.QHostAddress),
    "ipDetectionMode" to qVariant(state().ipDetectionMode, QuasselType.DccConfigIpDetectionMode),
    "portSelectionMode" to qVariant(state().portSelectionMode, QuasselType.DccConfigPortSelectionMode),
    "minPort" to qVariant(state().minPort, QtType.UShort),
    "maxPort" to qVariant(state().maxPort, QtType.UShort),
    "chunkSize" to qVariant(state().chunkSize, QtType.Int),
    "sendTimeout" to qVariant(state().sendTimeout, QtType.Int),
    "usePassiveDcc" to qVariant(state().usePassiveDcc, QtType.Bool),
    "useFastSend" to qVariant(state().useFastSend, QtType.Bool)
  )

  override fun setDccEnabled(enabled: Boolean) {
    state.update {
      copy(dccEnabled = enabled)
    }
    super.setDccEnabled(enabled)
  }

  override fun setOutgoingIp(outgoingIp: InetAddress) {
    state.update {
      copy(outgoingIp = outgoingIp)
    }
    super.setOutgoingIp(outgoingIp)
  }

  override fun setIpDetectionMode(ipDetectionMode: DccIpDetectionMode) {
    state.update {
      copy(ipDetectionMode = ipDetectionMode)
    }
    super.setIpDetectionMode(ipDetectionMode)
  }

  override fun setPortSelectionMode(portSelectionMode: DccPortSelectionMode) {
    state.update {
      copy(portSelectionMode = portSelectionMode)
    }
    super.setPortSelectionMode(portSelectionMode)
  }

  override fun setMinPort(port: UShort) {
    state.update {
      copy(minPort = port)
    }
    super.setMinPort(port)
  }

  override fun setMaxPort(port: UShort) {
    state.update {
      copy(maxPort = port)
    }
    super.setMaxPort(port)
  }

  override fun setChunkSize(chunkSize: Int) {
    state.update {
      copy(chunkSize = chunkSize)
    }
    super.setChunkSize(chunkSize)
  }

  override fun setSendTimeout(timeout: Int) {
    state.update {
      copy(sendTimeout = timeout)
    }
    super.setSendTimeout(timeout)
  }

  override fun setUsePassiveDcc(use: Boolean) {
    state.update {
      copy(usePassiveDcc = use)
    }
    super.setUsePassiveDcc(use)
  }

  override fun setUseFastSend(use: Boolean) {
    state.update {
      copy(useFastSend = use)
    }
    super.setUseFastSend(use)
  }

  fun isDccEnabled() = state().dccEnabled
  fun outgoingIp() = state().outgoingIp
  fun ipDetectionMode() = state().ipDetectionMode
  fun portSelectionMode() = state().portSelectionMode
  fun minPort() = state().minPort
  fun maxPort() = state().maxPort
  fun chunkSize() = state().chunkSize
  fun sendTimeout() = state().sendTimeout
  fun usePassiveDcc() = state().usePassiveDcc
  fun useFastSend() = state().useFastSend
}
