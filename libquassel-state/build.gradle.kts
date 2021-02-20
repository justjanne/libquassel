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
  id("com.google.devtools.ksp") version "1.4.30-1.0.0-alpha02"
}

dependencies {
  api(project(":libquassel-protocol"))
  ksp(project(":libquassel-generator"))
}