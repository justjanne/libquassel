import org.gradle.api.Project

/*
 * libquassel
 * Copyright (c) 2022 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

version = cmd("git", "describe", "--always", "--tags", "HEAD") ?: "1.0.0"

fun Project.cmd(vararg command: String) = try {
  val stdOut = java.io.ByteArrayOutputStream()
  exec {
    commandLine(*command)
    standardOutput = stdOut
  }
  stdOut.toString(Charsets.UTF_8.name()).trim()
} catch (e: Throwable) {
  null
}
