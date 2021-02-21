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

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import de.justjanne.libquassel.annotations.ProtocolSide
import de.justjanne.libquassel.annotations.SyncedObject
import de.justjanne.libquassel.generator.visitors.KSDeclarationParser
import de.justjanne.libquassel.generator.visitors.KotlinSaver
import de.justjanne.libquassel.generator.visitors.RpcModelProcessor

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
    resolver.getSymbolsWithAnnotation(SyncedObject::class.java.canonicalName)
      .mapNotNull { it.accept(KSDeclarationParser(resolver, logger), Unit) }
      .flatMap {
        listOfNotNull(
          it.accept(RpcModelProcessor(), ProtocolSide.CLIENT),
          it.accept(RpcModelProcessor(), ProtocolSide.CORE),
        )
      }
      .map { it.accept(KotlinSaver(), codeGenerator) }

    return emptyList()
  }

  override fun finish() = Unit
}
