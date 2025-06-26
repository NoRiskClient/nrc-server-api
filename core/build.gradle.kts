dependencies {
    implementation(libs.gson)
}

plugins {
    kotlin("jvm")
    id("java-library")
    id("maven-publish")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "gg.norisk.core.Core"
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "gg.norisk"
            artifactId = "server-api-core"
            version = rootProject.version.toString()
        }
    }
    repositories {
        mavenLocal()
    }
}
