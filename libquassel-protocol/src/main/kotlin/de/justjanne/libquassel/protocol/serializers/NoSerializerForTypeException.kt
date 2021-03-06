/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
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
    private val type: Int,
    private val javaType: Class<*>? = null
  ) : NoSerializerForTypeException() {
    override fun toString(): String {
      return "NoSerializerForTypeException.SignalProxy(type='$type', javaType=$javaType)"
    }
  }
}
