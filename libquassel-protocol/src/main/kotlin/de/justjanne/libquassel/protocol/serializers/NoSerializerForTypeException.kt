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

import de.justjanne.libquassel.protocol.models.types.QtType
import de.justjanne.libquassel.protocol.models.types.QuasselType

/**
 * Exception describing a missing serializer condition
 */
sealed class NoSerializerForTypeException : Exception() {
  /**
   * Exception describing a missing serializer condition for a qt type
   */
  data class Qt(
    private val type: Int,
    private val javaType: Class<*>? = null
  ) : NoSerializerForTypeException() {
    constructor(
      type: QtType,
      javaType: Class<*>? = null
    ) : this(type.id, javaType)

    override fun toString(): String {
      return "NoSerializerForTypeException.Qt(type=$type, javaType=$javaType)"
    }
  }

  /**
   * Exception describing a missing serializer condition for a quassel type
   */
  data class Quassel(
    private val type: Int,
    private val typename: String?,
    private val javaType: Class<*>? = null
  ) : NoSerializerForTypeException() {
    constructor(
      type: QtType,
      typename: String?,
      javaType: Class<*>? = null
    ) : this(type.id, typename, javaType)

    constructor(
      type: QuasselType,
      javaType: Class<*>? = null
    ) : this(type.qtType, type.typeName, javaType)

    override fun toString(): String {
      return "NoSerializerForTypeException.Quassel(type=$type, typename=$typename, javaType=$javaType)"
    }
  }

  /**
   * Exception describing a missing serializer condition for a handshake type
   */
  data class Handshake(
    private val type: String,
    private val javaType: Class<*>? = null
  ) : NoSerializerForTypeException() {
    override fun toString(): String {
      return "NoSerializerForTypeException.Handshake(type='$type', javaType=$javaType)"
    }
  }

  /**
   * Exception describing a missing serializer condition for a signal proxy type
   */
  data class SignalProxy(
    private val type: String,
    private val javaType: Class<*>? = null
  ) : NoSerializerForTypeException() {
    override fun toString(): String {
      return "NoSerializerForTypeException.Handshake(type='$type', javaType=$javaType)"
    }
  }
}
