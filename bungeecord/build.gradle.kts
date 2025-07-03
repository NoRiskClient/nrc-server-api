plugins {
    kotlin("jvm")
    id("java-library")
    alias(libs.plugins.shadow)
}

group = "gg.norisk"
version = "0.1.0"

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    mavenCentral()
}

dependencies {
    compileOnly(libs.bungeecord)
    implementation(project(":core"))
    implementation(libs.stdlib)
    implementation(libs.gson)
}

tasks {
    processResources {
        filesMatching("bungee.yml") {
            expand("version" to project.version)
        }
    }
    jar { enabled = false }
    shadowJar {
        dependsOn(jar)
        archiveBaseName.set("${rootProject.name}-bungeecord")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}