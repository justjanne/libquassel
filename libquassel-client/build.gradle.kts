/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

plugins {
  id("com.vanniktech.maven.publish")
}

dependencies {
  api(project(":libquassel-protocol"))

  val testcontainersCiVersion: String by project
  testImplementation("de.justjanne", "testcontainers-ci", testcontainersCiVersion)
  val sl4jVersion: String by project
  testImplementation("org.slf4j", "slf4j-simple", sl4jVersion)

  testImplementation(project(":libquassel-tests"))
}
