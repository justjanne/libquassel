/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

plugins {
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
  api(project(":protocol"))
  implementation("com.code-intelligence", "jazzer-api", "0.9.1")
}
