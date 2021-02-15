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

import de.justjanne.libquassel.protocol.features.LegacyFeatures
import de.justjanne.libquassel.protocol.features.QuasselFeatureName
import de.justjanne.libquassel.protocol.variant.QVariantList
import de.justjanne.libquassel.protocol.variant.QVariantMap

sealed class HandshakeMessage {
  data class ClientInit(
    val clientVersion: String?,
    val buildDate: String?,
    val clientFeatures: LegacyFeatures,
    val featureList: List<QuasselFeatureName>
  ) : HandshakeMessage()

  data class ClientInitAck(
    val coreFeatures: LegacyFeatures,
    val coreConfigured: Boolean?,
    val backendInfo: QVariantList,
    val authenticatorInfo: QVariantList,
    val featureList: List<QuasselFeatureName>
  ) : HandshakeMessage()

  data class ClientInitReject(
    val errorString: String?
  ) : HandshakeMessage()

  data class ClientLogin(
    val user: String?,
    val password: String?
  ) : HandshakeMessage()

  object ClientLoginAck : HandshakeMessage() {
    override fun toString(): String {
      return "ClientLoginAck"
    }
  }

  data class ClientLoginReject(
    val errorString: String?
  ) : HandshakeMessage()

  object CoreSetupAck : HandshakeMessage() {
    override fun toString(): String {
      return "CoreSetupAck"
    }
  }

  data class CoreSetupData(
    val adminUser: String?,
    val adminPassword: String?,
    val backend: String?,
    val setupData: QVariantMap?,
    val authenticator: String?,
    val authSetupData: QVariantMap?
  ) : HandshakeMessage()

  data class CoreSetupReject(
    val errorString: String?
  ) : HandshakeMessage()

  data class SessionInit(
    val identities: QVariantList?,
    val bufferInfos: QVariantList?,
    val networkIds: QVariantList?
  ) : HandshakeMessage()
}
