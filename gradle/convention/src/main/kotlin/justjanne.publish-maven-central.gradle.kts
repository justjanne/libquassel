plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

val canSign = project.properties.keys
    .any { it.startsWith("signing.") }

if (canSign) {
    nexusPublishing {
        repositories {
            sonatype {
                nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
                snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            }
        }
    }
}
