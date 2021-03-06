/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

plugins {
  `kotlin-dsl`
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

repositories {
  google()
  jcenter()
}

dependencies {
  implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.5.10")
  implementation("de.justjanne", "jacoco-cobertura-converter", "1.0.0")
  implementation(gradleApi())
  implementation(localGroovy())
}
