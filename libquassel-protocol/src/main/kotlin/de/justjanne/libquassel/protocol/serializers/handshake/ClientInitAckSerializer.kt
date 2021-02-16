/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.handshake

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.LegacyFeature
import de.justjanne.libquassel.protocol.features.QuasselFeatureName
import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.models.QStringList
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.HandshakeSerializer
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

/**
 * Serializer for [HandshakeMessage.ClientInitAck]
 */
object ClientInitAckSerializer : HandshakeSerializer<HandshakeMessage.ClientInitAck> {
  override val type: String = "ClientInitAck"

  override fun serialize(data: HandshakeMessage.ClientInitAck) = mapOf(
    "MsgType" to qVariant(type, QtType.QString),
    "CoreFeatures" to qVariant(data.coreFeatures.toBits(), QtType.UInt),
    "StorageBackends" to qVariant(data.backendInfo, QtType.QVariantList),
    "Authenticator" to qVariant(data.authenticatorInfo, QtType.QVariantList),
    "Configured" to qVariant(data.coreConfigured, QtType.Bool),
    "FeatureList" to qVariant(data.featureList, QtType.QStringList)
  )

  override fun deserialize(data: QVariantMap) = HandshakeMessage.ClientInitAck(
    coreConfigured = data["Configured"].into(),
    backendInfo = data["StorageBackends"].into(emptyList()),
    authenticatorInfo = data["Authenticators"].into(emptyList()),
    coreFeatures = LegacyFeature.of(data["CoreFeatures"].into<UInt>()),
    featureList = data["FeatureList"].into<QStringList>(emptyList())
      .filterNotNull()
      .map(::QuasselFeatureName),
  )
}
