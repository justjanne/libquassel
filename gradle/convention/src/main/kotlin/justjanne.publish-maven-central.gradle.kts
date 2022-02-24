plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

val canSign = project.properties.keys
    .any { it.startsWith("signing.") }

if (canSign) {
    nexusPublishing {
        repositories {
            sonatype()
        }
    }
}
