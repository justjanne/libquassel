/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.generator

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.annotations.SyncedCall
import de.justjanne.libquassel.annotations.SyncedObject
import de.justjanne.libquassel.generator.annotation.SyncedCallAnnotationModel
import de.justjanne.libquassel.generator.annotation.SyncedObjectAnnotationModel
import de.justjanne.libquassel.generator.model.SyncedCallModel
import de.justjanne.libquassel.generator.model.SyncedObjectModel
import de.justjanne.libquassel.generator.model.SyncedParameterModel
import de.justjanne.libquassel.generator.util.findAnnotationWithType
import de.justjanne.libquassel.generator.util.getMember
import de.justjanne.libquassel.generator.util.toEnum

class InvokerProcessor : SymbolProcessor {
  lateinit var codeGenerator: CodeGenerator
  lateinit var logger: KSPLogger

  override fun init(
    options: Map<String, String>,
    kotlinVersion: KotlinVersion,
    codeGenerator: CodeGenerator,
    logger: KSPLogger
  ) {
    this.logger = logger
    this.codeGenerator = codeGenerator
  }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val classes = resolver.getSymbolsWithAnnotation(SyncedObject::class.java.canonicalName)
      .flatMap {
        val visitor = QuasselRpcVisitor(resolver)
        it.accept(visitor, Unit)
        visitor.classes
      }

    val message = classes.flatMap {
      listOf(
        "@SyncedObject(name=${it.rpcName})",
        it.name,
      ) + it.methods.flatMap {
        listOf(
          "@SyncedCall(name=${it.rpcName}, target=${it.side})",
          it.name,
        ) + it.parameters.flatMap {
          listOf(
            "${it.name}: ${it.type}"
          )
        }.map { "  $it" }
      }.map { "  $it" }
    }.joinToString("\n")
    logger.logging("Data: \n\n$message\n\n")

    return emptyList()
  }

  override fun finish() {
    // TODO
  }

  inner class QuasselRpcVisitor(
    private val resolver: Resolver
  ) : KSVisitorVoid() {
    val classes = mutableListOf<SyncedObjectModel>()
    var functions = mutableListOf<SyncedCallModel>()
    var parameters = mutableListOf<SyncedParameterModel>()

    private fun collectFunctions(): List<SyncedCallModel> {
      val result = functions
      functions = mutableListOf()
      return result
    }

    private fun collectParameters(): List<SyncedParameterModel> {
      val result = parameters
      parameters = mutableListOf()
      return result
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
      try {
        val annotation = classDeclaration.findAnnotationWithType<SyncedObject>(resolver) ?: return
        val syncedObjectModel = SyncedObjectAnnotationModel(
          name = annotation.getMember("name"),
        )
        classDeclaration.getDeclaredFunctions().map { it.accept(this, Unit) }
        classes.add(
          SyncedObjectModel(
            classDeclaration.qualifiedName?.asString() ?: return,
            syncedObjectModel.name,
            collectFunctions()
          )
        )
      } catch (t: Throwable) {
        logger.error("Error processing class ${classDeclaration.qualifiedName?.asString()}", classDeclaration)
        logger.exception(t)
      }
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
      val annotation = function.findAnnotationWithType<SyncedCall>(resolver) ?: return
      try {
        val syncedCallModel = SyncedCallAnnotationModel(
          name = annotation.getMember("name"),
          target = annotation.getMember<KSType>("target")?.toEnum<ProtocolSide>(),
        )
        function.parameters.map { it.accept(this, Unit) }
        functions.add(
          SyncedCallModel(
            function.simpleName.asString(),
            syncedCallModel.name,
            syncedCallModel.target,
            collectParameters()
          )
        )
      } catch (t: Throwable) {
        logger.error("Error processing function ${function.qualifiedName?.asString()}", function)
        logger.exception(t)
      }
    }

    override fun visitValueParameter(valueParameter: KSValueParameter, data: Unit) {
      try {
        super.visitValueParameter(valueParameter, data)
        parameters.add(
          SyncedParameterModel(
            valueParameter.name?.asString(),
            valueParameter.type.resolve()
          )
        )
      } catch (t: Throwable) {
        logger.error("Error processing parameter ${valueParameter.name?.asString()}", valueParameter)
        logger.exception(t)
      }
    }
  }
}
