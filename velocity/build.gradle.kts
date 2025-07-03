plugins {
    kotlin("jvm")
    id("java-library")
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    implementation(libs.velocity)
    implementation(project(":core"))
    implementation(libs.stdlib)
    implementation(libs.gson)
    annotationProcessor(libs.velocity)
}

tasks {
    processResources {
        filesMatching("velocity-plugin.json") {
            expand("version" to project.version)
        }
    }
    jar { enabled = false }
    shadowJar {
        dependsOn(jar)
        archiveBaseName.set("${rootProject.name}-velocity")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}
