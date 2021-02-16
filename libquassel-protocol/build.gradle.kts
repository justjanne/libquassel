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
  api("org.threeten", "threetenbp", "1.4.0")
  val kotlinBitflagsVersion: String by project
  api("de.justjanne", "kotlin-bitflags", kotlinBitflagsVersion)
  api(project(":libquassel-annotations"))
}
