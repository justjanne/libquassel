/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.handshake

import de.justjanne.bitflags.of
import de.justjanne.bitflags.toBits
import de.justjanne.libquassel.protocol.features.FeatureSet
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
 * Serializer for [HandshakeMessage.ClientInit]
 */
object ClientInitSerializer : HandshakeSerializer<HandshakeMessage.ClientInit> {
  override val type: String = "ClientInit"

  override fun serialize(data: HandshakeMessage.ClientInit) = mapOf(
    "MsgType" to qVariant(type, QtType.QString),
    "ClientVersion" to qVariant(data.clientVersion, QtType.QString),
    "ClientDate" to qVariant(data.buildDate, QtType.QString),
    "Features" to qVariant(data.featureSet.legacyFeatures().toBits(), QtType.UInt),
    "FeatureList" to qVariant(
      data.featureSet.featureList().map(QuasselFeatureName::name),
      QtType.QStringList
    ),
  )

  override fun deserialize(data: QVariantMap) = HandshakeMessage.ClientInit(
    clientVersion = data["ClientVersion"].into(),
    buildDate = data["ClientDate"].into(),
    featureSet = FeatureSet.build(
      LegacyFeature.of(data["Features"].into<UInt>()),
      data["FeatureList"].into<QStringList>(emptyList())
        .filterNotNull()
        .map(::QuasselFeatureName)
    )
  )
}
