import de.justjanne.coverageconverter.CoverageConverterExtension

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "10.0.0" apply false
  id("com.vanniktech.maven.publish") version "0.13.0" apply false
  id("de.justjanne.jacoco-cobertura-converter") apply false
}

buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.20")
    classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.4.30")
  }
}

allprojects {
  apply(plugin = "org.jetbrains.dokka")
  repositories {
    mavenCentral()
    exclusiveContent {
      forRepository {
        maven {
          name = "JCenter"
          setUrl("https://jcenter.bintray.com/")
        }
      }
      filter {
        // Required for Dokka
        includeModule("com.soywiz.korlibs.korte", "korte-jvm")
        includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
        includeGroup("org.jetbrains.dokka")
        includeModule("org.jetbrains", "markdown")
      }
    }
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
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.4.2")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-test", "1.4.2")

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

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs = listOf(
        "-Xinline-classes",
        "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
      )
    }
  }

  configure<CoverageConverterExtension> {
    autoConfigureCoverage = true
  }

  configure<JavaPluginExtension> {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(11))
    }
  }
}
