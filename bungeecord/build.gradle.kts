plugins {
    id("java-library")
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.bungeecord)
    implementation(project(":core"))
}

tasks {
    jar { enabled = false }
    shadowJar {
        dependsOn(":jar")
        archiveBaseName.set("${rootProject.name}-bungeecord")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}