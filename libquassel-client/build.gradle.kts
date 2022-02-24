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
  id("jacoco-report-aggregation")
}

dependencies {
  api(project(":libquassel-protocol"))
  testImplementation(libs.testcontainers)
  implementation(libs.slf4j)
}
tasks.check {
  dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}
