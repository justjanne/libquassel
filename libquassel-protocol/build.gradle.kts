plugins {
  id("com.vanniktech.maven.publish")
}

dependencies {
  api("org.threeten", "threetenbp", "1.4.0")
  val kotlinBitflagsVersion: String by project
  api("de.justjanne", "kotlin-bitflags", kotlinBitflagsVersion)
  api(project(":libquassel-annotations"))

  testImplementation(project(":libquassel-tests"))
}
