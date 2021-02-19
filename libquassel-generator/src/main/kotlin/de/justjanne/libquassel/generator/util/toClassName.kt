/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.generator.util

import com.google.devtools.ksp.isLocal
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName

internal fun KSType.toClassName(): ClassName {
  val decl = declaration
  check(decl is KSClassDeclaration)
  return decl.toClassName()
}

internal fun KSClassDeclaration.toClassName(): ClassName {
  require(!isLocal()) {
    "Local/anonymous classes are not supported!"
  }
  val pkgName = packageName.asString()
  val typesString = qualifiedName!!.asString().removePrefix("$pkgName.")

  val simpleNames = typesString
    .split(".")
  return ClassName(pkgName, simpleNames)
}

fun Class<*>.toClassName(): ClassName {
  val packageName = `package`.name
  val names = canonicalName.substring(packageName.length)
    .trimStart('.')
    .split('.')
  return ClassName(packageName, names)
}
