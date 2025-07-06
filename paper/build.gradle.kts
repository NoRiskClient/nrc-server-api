plugins {
    id("java-library")
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.spigot)
    implementation(project(":core"))
}

tasks {
    processResources {
        filesMatching("paper-plugin.yml") {
            expand("version" to version.toString())
        }
    }
    jar { enabled = false }
    shadowJar {
        dependsOn(":jar")
        archiveBaseName.set("${rootProject.name}-paper")
        archiveVersion.set(version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
}