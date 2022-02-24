/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

plugins {
  id("justjanne.kotlin")
  id("justjanne.publication")
}

dependencies {
  api(project(":libquassel-annotations"))
  ksp(project(":libquassel-generator"))
  api(libs.threetenbp)
  api(libs.kotlin.bitflags)
  implementation(libs.bouncycastle)
  implementation(libs.slf4j)
  testImplementation(libs.hamcrest)
}
