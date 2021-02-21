/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

rootProject.name = "libquassel"

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
  }
}
