/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.generator.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import de.justjanne.libquassel.generator.kotlinmodel.KotlinModel
import de.justjanne.libquassel.generator.kotlinmodel.KotlinModelVisitor

class KotlinSaver : KotlinModelVisitor<CodeGenerator, Unit> {
  override fun visitFileModel(model: KotlinModel.FileModel, data: CodeGenerator) {
    data.createNewFile(
      Dependencies(false, model.source.containingFile!!),
      model.data.packageName,
      model.data.name
    ).bufferedWriter(Charsets.UTF_8).use {
      model.data.writeTo(it)
    }
  }

  override fun visitFunctionModel(
    model: KotlinModel.FunctionModel,
    data: CodeGenerator
  ) = Unit
}
