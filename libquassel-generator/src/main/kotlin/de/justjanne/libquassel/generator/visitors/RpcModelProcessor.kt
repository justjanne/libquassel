/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.generator.visitors

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.generator.kotlinmodel.KotlinModel
import de.justjanne.libquassel.generator.rpcmodel.RpcModel
import de.justjanne.libquassel.generator.rpcmodel.RpcModelVisitor
import de.justjanne.libquassel.generator.util.kotlinpoet.ArgString
import de.justjanne.libquassel.generator.util.kotlinpoet.buildWhen
import de.justjanne.libquassel.generator.util.kotlinpoet.withIndent

class RpcModelProcessor : RpcModelVisitor<ProtocolSide, KotlinModel?> {
  override fun visitObjectModel(model: RpcModel.ObjectModel, data: ProtocolSide): KotlinModel {
    val name = ClassName(
      TYPENAME_INVOKER.packageName,
      "${model.rpcName}${data.name.toLowerCase().capitalize()}Invoker"
    )
    return KotlinModel.FileModel(
      model.source,
      FileSpec.builder(name.packageName, name.simpleName)
        .addImport(
          TYPENAME_QVARIANT_INTOORTHROW.packageName,
          TYPENAME_QVARIANT_INTOORTHROW.simpleName
        )
        .addType(
          TypeSpec.objectBuilder(name.simpleName)
            .addSuperinterface(TYPENAME_INVOKER.parameterizedBy(model.name))
            .addProperty(
              PropertySpec.builder(
                "className",
                String::class.asTypeName(),
                KModifier.OVERRIDE
              ).initializer("\"${model.rpcName}\"").build()
            )
            .addFunction(
              FunSpec.builder("invoke")
                .addModifiers(KModifier.OVERRIDE, KModifier.OPERATOR)
                .addParameter(
                  ParameterSpec.builder(
                    "on",
                    TYPENAME_ANY
                  ).build()
                ).addParameter(
                  ParameterSpec.builder(
                    "method",
                    String::class.asTypeName()
                  ).build()
                ).addParameter(
                  ParameterSpec.builder(
                    "params",
                    TYPENAME_QVARIANTLIST
                  ).build()
                )
                .addCode(
                  buildCodeBlock {
                    beginControlFlow("if (on is %T)", model.name)
                    buildWhen("method") {
                      for (method in model.methods) {
                        val block = method.accept(this@RpcModelProcessor, data)
                          as? KotlinModel.FunctionModel
                          ?: continue
                        addCase(ArgString("%S", method.rpcName ?: method.name), block.data)
                      }
                      buildElse {
                        addStatement("throw %T(className, method)", TYPENAME_UNKNOWN_METHOD_EXCEPTION)
                      }
                    }
                    nextControlFlow("else")
                    addStatement("throw %T(on, className)", TYPENAME_WRONG_OBJECT_TYPE_EXCEPTION)
                    endControlFlow()
                  }
                )
                .build()
            )
            .build()
        ).build()
    )
  }

  override fun visitFunctionModel(model: RpcModel.FunctionModel, data: ProtocolSide) =
    if (model.side != data) null
    else KotlinModel.FunctionModel(
      model.source,
      buildCodeBlock {
        if (model.parameters.isEmpty()) {
          addStatement("on.${model.name}()")
        } else {
          addStatement("on.${model.name}(")
          withIndent {
            val lastIndex = model.parameters.size - 1
            for ((i, parameter) in model.parameters.withIndex()) {
              val suffix = if (i != lastIndex) "," else ""
              addStatement(
                "${parameter.name} = params[$i].intoOrThrow<%T>()$suffix",
                parameter.type
              )
            }
          }
          addStatement(")")
        }
      }
    )

  override fun visitParameterModel(model: RpcModel.ParameterModel, data: ProtocolSide): KotlinModel? = null

  companion object {
    private val TYPENAME_INVOKER = ClassName(
      "de.justjanne.libquassel.state.invoker",
      "Invoker"
    )
    private val TYPENAME_UNKNOWN_METHOD_EXCEPTION = ClassName(
      "de.justjanne.libquassel.state.exceptions",
      "UnknownMethodException"
    )
    private val TYPENAME_WRONG_OBJECT_TYPE_EXCEPTION = ClassName(
      "de.justjanne.libquassel.state.exceptions",
      "WrongObjectTypeException"
    )
    private val TYPENAME_QVARIANTLIST = ClassName(
      "de.justjanne.libquassel.protocol.variant",
      "QVariantList"
    )
    private val TYPENAME_QVARIANT_INTOORTHROW = ClassName(
      "de.justjanne.libquassel.protocol.variant",
      "intoOrThrow"
    )
    private val TYPENAME_ANY = ANY.copy(nullable = true)

    init {
      System.setProperty("idea.io.use.nio2", "true")
    }
  }
}
