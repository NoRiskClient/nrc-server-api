plugins {
    kotlin("jvm")
    id("java-library")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.spigot)
    implementation(project(":core"))
    implementation(libs.stdlib)
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
    jar { enabled = false }
    shadowJar {
        dependsOn(jar)
        archiveBaseName.set("${rootProject.name}-paper")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}