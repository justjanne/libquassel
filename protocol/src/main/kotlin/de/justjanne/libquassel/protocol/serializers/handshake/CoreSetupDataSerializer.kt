/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers.handshake

import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.HandshakeSerializer
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

/**
 * Serializer for [HandshakeMessage.CoreSetupData]
 */
object CoreSetupDataSerializer : HandshakeSerializer<HandshakeMessage.CoreSetupData> {
  override val type: String = "CoreSetupData"

  override fun serialize(data: HandshakeMessage.CoreSetupData) = mapOf(
    "MsgType" to qVariant(type, QtType.QString),
    "SetupData" to qVariant(
      mapOf(
        "AdminUser" to qVariant(data.adminUser, QtType.QString),
        "AdminPasswd" to qVariant(data.adminPassword, QtType.QString),
        "Backend" to qVariant(data.backend, QtType.QString),
        "ConnectionProperties" to qVariant(data.setupData, QtType.QVariantMap),
        "Authenticator" to qVariant(data.authenticator, QtType.QString),
        "AuthProperties" to qVariant(data.authSetupData, QtType.QVariantMap),
      ),
      QtType.QVariantMap
    )
  )

  override fun deserialize(data: QVariantMap) =
    data["SetupData"].into<QVariantMap>().orEmpty().let {
      HandshakeMessage.CoreSetupData(
        adminUser = it["AdminUser"].into(),
        adminPassword = it["AdminPasswd"].into(),
        backend = it["Backend"].into(),
        setupData = it["ConnectionProperties"].into<QVariantMap>().orEmpty(),
        authenticator = it["Authenticator"].into(),
        authSetupData = it["AuthProperties"].into<QVariantMap>().orEmpty()
      )
    }
}
