/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

repositories {
  google()
}

dependencies {
  val kspVersion: String by project
  implementation("com.google.devtools.ksp", "symbol-processing-api", kspVersion)
  implementation(project(":annotations"))
  val kotlinPoetVersion: String by project
  implementation("com.squareup", "kotlinpoet", kotlinPoetVersion)
}
