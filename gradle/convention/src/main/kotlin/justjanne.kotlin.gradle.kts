import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("justjanne.java")
  id("justjanne.dokka")
  id("justjanne.ktlint")
  id("com.google.devtools.ksp")
  kotlin("jvm")
  kotlin("kapt")
}

repositories {
  mavenCentral()
  google()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

  testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.8.2")
  testImplementation("org.junit.jupiter", "junit-jupiter-params", "5.8.2")
  testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")

  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.6.10")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf(
      "-Xinline-classes",
      "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
    )
    jvmTarget = "1.8"
  }
}
