plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  google()
}

dependencies {
  implementation("io.github.gradle-nexus:publish-plugin:1.1.0")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
  implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.6.10-1.0.4")
  implementation("org.jlleitschuh.gradle:ktlint-gradle:10.2.1")
}

configure<JavaPluginExtension> {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}
