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
