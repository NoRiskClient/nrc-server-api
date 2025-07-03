plugins {
    kotlin("kapt")
    kotlin("jvm")
    id("java-library")
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    compileOnly(libs.velocity)
    implementation(project(":core"))
    implementation(libs.stdlib)
    implementation(libs.gson)
    kapt(libs.velocity)
}

tasks {
    jar { enabled = false }
    shadowJar {
        val projectVersion = project.version
        dependsOn(jar)
        archiveBaseName.set("${rootProject.name}-velocity")
        archiveVersion.set(projectVersion.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}
