/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

plugins {
  id("java-library")
  id("com.vanniktech.maven.publish")
  id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha02"
}

dependencies {
  api("org.threeten", "threetenbp", "1.4.0")
  val kotlinBitflagsVersion: String by project
  api("de.justjanne", "kotlin-bitflags", kotlinBitflagsVersion)
  val bouncyCastleVersion: String by project
  implementation("org.bouncycastle", "bcpkix-jdk15on", bouncyCastleVersion)
  api(project(":libquassel-annotations"))
  ksp(project(":libquassel-generator"))
}
