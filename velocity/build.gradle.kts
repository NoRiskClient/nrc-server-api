plugins {
    id("java-library")
    alias(libs.plugins.shadow)
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

dependencies {
    compileOnly(libs.velocity)
    implementation(project(":core"))
    annotationProcessor(libs.velocity)
}

tasks {
    jar { enabled = false }
    shadowJar {
        dependsOn(":jar") // Explizite Abhängigkeit hinzufügen
        archiveBaseName.set("${rootProject.name}-velocity")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}