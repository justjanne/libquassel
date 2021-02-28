/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers

import de.justjanne.libquassel.protocol.models.HandshakeMessage
import de.justjanne.libquassel.protocol.variant.QVariantMap

/**
 * High-level serializer for handshake messages.
 */
interface HandshakeSerializer<T : HandshakeMessage> {
  /**
   * The underlying handshake message type this serializer can (de-)serialize.
   * Used for type-safe serializer autodiscovery.
   */
  val type: String

  /**
   * Serialize a handshake message into a [QVariantMap] (for further serialization)
   */
  fun serialize(data: T): QVariantMap

  /**
   * Deserialize a handshake message from a [QVariantMap]
   */
  fun deserialize(data: QVariantMap): T
}
