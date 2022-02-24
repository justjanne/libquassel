plugins {
  java
  jacoco
}

version = rootProject.version
group = rootProject.group

tasks.getByName("jacocoTestReport") {
  enabled = false
}

tasks.withType<JavaCompile> {
  sourceCompatibility = "1.8"
  targetCompatibility = "1.8"
}

tasks.withType<Test> {
  useJUnitPlatform()
}

configure<JavaPluginExtension> {
  withJavadocJar()
  withSourcesJar()

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}
