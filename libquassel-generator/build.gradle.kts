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
  implementation("com.google.devtools.ksp:symbol-processing-api:1.4.30-1.0.0-alpha02")
  implementation(project(":libquassel-annotations"))
  implementation("com.squareup", "kotlinpoet", "1.7.2")
}
