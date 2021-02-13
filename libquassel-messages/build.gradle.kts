plugins {
  id("com.vanniktech.maven.publish")
}

dependencies {
  api(project(":libquassel-protocol"))

  val testcontainersCiVersion: String by project
  testImplementation("de.justjanne", "testcontainers-ci", testcontainersCiVersion)
  val sl4jVersion: String by project
  testImplementation("org.slf4j", "slf4j-simple", sl4jVersion)
}
