/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "libquassel"

includeBuild("gradle/convention")

include(
  ":libquassel-annotations",
  ":libquassel-protocol",
  ":libquassel-generator",
  ":libquassel-client"
)

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
