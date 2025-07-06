import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaLibraryPlugin

plugins {
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

tasks.jar {
    enabled = false
}

subprojects {
    version = rootProject.version
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven(uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots"))
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks {
        jar {
            enabled = true
            archiveBaseName.set(rootProject.name)
            archiveVersion.set(project.version.toString())
            archiveClassifier.set("")
            destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
        }
    }

    dependencies {
        if (project.name != "core") {
            implementation(project(":core"))
        }
    }
}