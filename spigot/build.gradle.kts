plugins {
    id("java-library")
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.spigot)
    implementation(project(":core"))
}

tasks {
    jar { enabled = false }
    shadowJar {
        dependsOn(jar)
        archiveBaseName.set("${rootProject.name}-spigot")
        archiveVersion.set(version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}