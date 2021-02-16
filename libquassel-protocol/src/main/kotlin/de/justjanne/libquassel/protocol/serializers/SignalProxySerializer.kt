/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.serializers

import de.justjanne.libquassel.protocol.models.SignalProxyMessage
import de.justjanne.libquassel.protocol.variant.QVariantList

/**
 * High-level serializer for signal proxy messages.
 */
interface SignalProxySerializer<T : SignalProxyMessage> {
  /**
   * The underlying signal proxy message type this serializer can (de-)serialize.
   * Used for type-safe serializer autodiscovery.
   */
  val type: Int

  /**
   * Serialize a signal proxy message into a [QVariantList] (for further serialization)
   */
  fun serialize(data: T): QVariantList

  /**
   * Deserialize a signal proxy message from a [QVariantList]
   */
  fun deserialize(data: QVariantList): T
}
