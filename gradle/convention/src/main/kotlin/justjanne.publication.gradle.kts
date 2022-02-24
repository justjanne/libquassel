/*
 * libquassel
 * Copyright (c) 2022 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

plugins {
  id("maven-publish")
  id("signing")
}

version = rootProject.version
group = rootProject.group

val canSign = project.properties.keys
  .any { it.startsWith("signing.") }

publishing {
  publications {
    create<MavenPublication>("maven") {
      publication()
      pom()
    }
  }
}

configure<SigningExtension> {
  if (canSign) {
    useGpgCmd()
    sign(publishing.publications["maven"])
  }
}

fun MavenPublication.pom() {
  pom {
    name.set(buildHumanReadableName(artifactId))
    description.set("Pure-Kotlin implementation of the Quassel Protocol")
    url.set("https://git.kuschku.de/justJanne/libquassel")
    licenses {
      license {
        name.set("Mozilla Public License Version 2.0")
        url.set("https://www.mozilla.org/en-US/MPL/2.0/")
      }
    }
    developers {
      developer {
        id.set("justJanne")
        name.set("Janne Mareike Koschinski")
      }
    }
    scm {
      connection.set("scm:git:https://git.kuschku.de/justJanne/libquassel.git")
      developerConnection.set("scm:git:ssh://git.kuschku.de:2222/justJanne/libquassel.git")
      url.set("https://git.kuschku.de/justJanne/libquassel")
    }
  }
}

fun MavenPublication.publication() {
  val projectName = project.name
    .removePrefix("core")
    .removePrefix("-")
  artifactId = buildArtifactName(
    extractArtifactGroup(project.group as String),
    rootProject.name,
    projectName.ifEmpty { null }
  )
  from(components["java"])
}

fun buildArtifactName(group: String? = null, project: String? = null, module: String? = null): String {
  return removeConsecutive(listOfNotNull(group, project, module).flatMap { it.split('-') })
    .joinToString("-")
}

fun buildHumanReadableName(name: String) = name
  .splitToSequence('-')
  .joinToString(" ", transform = String::capitalize)

fun extractArtifactGroup(group: String): String? {
  // split into parts by domain separator
  val elements = group.split('.')
  // drop the tld/domain part, e.g. io.datalbry
  val withoutDomain = elements.drop(2)
  // if anything remains, that’s our artifact group
  return withoutDomain.lastOrNull()
}

fun <T> removeConsecutive(list: List<T>): List<T> {
  val result = mutableListOf<T>()
  for (el in list) {
    if (el != result.lastOrNull()) {
      result.add(el)
    }
  }
  return result
}
