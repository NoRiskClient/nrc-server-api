plugins {
    kotlin("jvm")
    id("java-library")
}

dependencies {
    implementation(libs.spigot)
    implementation(project(":core"))
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
    jar {
        enabled = true
        archiveBaseName.set("${rootProject.name}-spigot")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
        manifest {
            attributes("Main-Class" to "gg.norisk.spigot.Spigot")
        }
    }
}