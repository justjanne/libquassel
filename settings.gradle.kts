/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

rootProject.name = "libquassel"
rootProject.children.forEach {
  it.name = rootProject.name + "-" + it.name
}

include(
  ":annotations",
  ":protocol",
  ":generator",
  ":client",
  ":fuzz"
)

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
