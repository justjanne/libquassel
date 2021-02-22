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

package de.justjanne.libquassel.messages.handshake

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.LegacyFeature
import de.justjanne.libquassel.protocol.features.QuasselFeatureName
import de.justjanne.libquassel.protocol.variant.QStringList
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.QtType
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

object ClientInitAckSerializer : HandshakeSerializer<HandshakeMessage.ClientInitAck> {
  override val type: String = "ClientInitAck"
  override val javaType: Class<out HandshakeMessage.ClientInitAck> =
    HandshakeMessage.ClientInitAck::class.java

  override fun serialize(data: HandshakeMessage.ClientInitAck) = mapOf(
    "MsgType" to qVariant(type, QtType.QString),
    "CoreFeatures" to qVariant(data.coreFeatures.toBits(), QtType.UInt),
    "StorageBackends" to qVariant(data.backendInfo, QtType.QVariantList),
    "Authenticator" to qVariant(data.authenticatorInfo, QtType.QVariantList),
    "Configured" to qVariant(data.coreConfigured, QtType.Bool),
    "FeatureList" to qVariant(data.featureList, QtType.QStringList)
  )

  override fun deserialize(data: QVariantMap) = HandshakeMessage.ClientInitAck(
    coreFeatures = LegacyFeature.of(data["CoreFeatures"].into<UInt>()),
    backendInfo = data["StorageBackends"].into(emptyList()),
    authenticatorInfo = data["Authenticators"].into(emptyList()),
    coreConfigured = data["Configured"].into(),
    featureList = data["FeatureList"].into<QStringList>(emptyList())
      .filterNotNull()
      .map(::QuasselFeatureName),
  )
}
