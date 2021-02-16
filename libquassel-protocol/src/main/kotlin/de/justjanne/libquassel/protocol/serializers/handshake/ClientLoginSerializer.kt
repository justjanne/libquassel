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

import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.serializers.HandshakeSerializer
import de.justjanne.libquassel.protocol.variant.QVariantMap
import de.justjanne.libquassel.protocol.variant.into
import de.justjanne.libquassel.protocol.variant.qVariant

/**
 * Serializer for [HandshakeMessage.ClientLogin]
 */
object ClientLoginSerializer : HandshakeSerializer<HandshakeMessage.ClientLogin> {
  override val type: String = "ClientLogin"

  override fun serialize(data: HandshakeMessage.ClientLogin) = mapOf(
    "MsgType" to qVariant(type, QtType.QString),
    "User" to qVariant(data.user, QtType.QString),
    "Password" to qVariant(data.password, QtType.QString)
  )

  override fun deserialize(data: QVariantMap) = HandshakeMessage.ClientLogin(
    user = data["User"].into(),
    password = data["Password"].into(),
  )
}
