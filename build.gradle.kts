import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaLibraryPlugin

plugins {
    kotlin("jvm") version libs.versions.kotlinPlugin.get()
    id("java")
    id("maven-publish")
}

allprojects {
    group = "gg.norisk"
    version = "0.1.0"
}

repositories {
    mavenCentral()
}

val stdlib: String = libs.stdlib.get().toString()

tasks.jar {
    enabled = false
}

subprojects {
    version = rootProject.version
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven(uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots"))
        mavenCentral()
    }

    kotlin {
        jvmToolchain(21)
    }

    tasks {
        jar {
            enabled = true
            archiveBaseName.set(rootProject.name)
            archiveVersion.set(project.version.toString())
            archiveClassifier.set("")
            destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
            manifest {
                attributes("Main-Class" to "gg.norisk.core.NRCServerApi")
            }
        }
    }

    dependencies {
        implementation(stdlib)
        if (project.name != "core") {
            implementation(project(":core"))
        }
    }
}