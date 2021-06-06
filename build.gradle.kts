/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

import de.justjanne.coverageconverter.CoverageConverterExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "10.0.0" apply false
  id("com.vanniktech.maven.publish") version "0.13.0" apply false
  id("de.justjanne.jacoco-cobertura-converter") apply false
  id("org.jetbrains.dokka") version "1.4.32"
}

allprojects {
  apply(plugin = "org.jetbrains.dokka")

  repositories {
    mavenCentral()
    google()
  }
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")
  apply(plugin = "jacoco")
  apply(plugin = "de.justjanne.jacoco-cobertura-converter")

  dependencies {
    val implementation by configurations
    val testImplementation by configurations
    val testRuntimeOnly by configurations

    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit5"))

    val kotlinxCoroutinesVersion: String by project
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinxCoroutinesVersion)
    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-test", kotlinxCoroutinesVersion)

    val junit5Version: String by project
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junit5Version)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junit5Version)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junit5Version)

    val hamcrestVersion: String by project
    testImplementation("org.hamcrest", "hamcrest-library", hamcrestVersion)
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs = listOf(
        "-Xinline-classes",
        "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
      )
    }
  }

  configure<JacocoPluginExtension> {
    toolVersion = "0.8.7"
  }

  configure<CoverageConverterExtension> {
    autoConfigureCoverage = true
  }

  configure<JavaPluginExtension> {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(8))
    }
  }
}
