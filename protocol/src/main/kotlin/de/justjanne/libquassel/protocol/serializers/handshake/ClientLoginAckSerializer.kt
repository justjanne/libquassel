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
import de.justjanne.libquassel.protocol.variant.qVariant

/**
 * Serializer for [HandshakeMessage.ClientLoginAck]
 */
object ClientLoginAckSerializer : HandshakeSerializer<HandshakeMessage.ClientLoginAck> {
  override val type: String = "ClientLoginAck"

  override fun serialize(data: HandshakeMessage.ClientLoginAck) = mapOf(
    "MsgType" to qVariant(type, QtType.QString),
  )

  override fun deserialize(data: QVariantMap) = HandshakeMessage.ClientLoginAck
}
