/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.generator

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.generator.rpcmodel.RpcModel

object Constants {
  fun invokerName(model: RpcModel.ObjectModel, side: ProtocolSide) = ClassName(
    TYPENAME_INVOKER.packageName,
    "${model.rpcName}${side.name.toLowerCase().capitalize()}Invoker"
  )

  val TYPENAME_ANY = ANY.copy(nullable = true)
  val TYPENAME_SYNCABLESTUB = ClassName(
    "de.justjanne.libquassel.protocol.syncables",
    "SyncableStub"
  )
  val TYPENAME_INVOKER = ClassName(
    "de.justjanne.libquassel.protocol.syncables.invoker",
    "Invoker"
  )
  val TYPENAME_INVOKERREGISTRY = ClassName(
    "de.justjanne.libquassel.protocol.syncables.invoker",
    "InvokerRegistry"
  )
  val TYPENAME_INVOKERMAP = MAP.parameterizedBy(STRING, TYPENAME_INVOKER)
  val TYPENAME_UNKNOWN_METHOD_EXCEPTION = ClassName(
    "de.justjanne.libquassel.protocol.exceptions",
    "RpcInvocationFailedException", "UnknownMethodException"
  )
  val TYPENAME_WRONG_OBJECT_TYPE_EXCEPTION = ClassName(
    "de.justjanne.libquassel.protocol.exceptions",
    "RpcInvocationFailedException", "WrongObjectTypeException"
  )
  val TYPENAME_QVARIANTLIST = ClassName(
    "de.justjanne.libquassel.protocol.variant",
    "QVariantList"
  )
  val TYPENAME_QVARIANT_INTOORTHROW = ClassName(
    "de.justjanne.libquassel.protocol.variant",
    "intoOrThrow"
  )
  val TYPENAME_GENERATED = ClassName(
    "de.justjanne.libquassel.annotations",
    "Generated"
  )

  init {
    System.setProperty("idea.io.use.nio2", "true")
  }
}
