import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaLibraryPlugin

plugins {
    kotlin("jvm") version libs.versions.kotlinPlugin.get()
    alias(libs.plugins.shadow)
    id("java")
    id("org.gradle.wrapper")
}

group = "gg.norisk"
version = "0.1.0"

repositories {
    mavenCentral()
}

val stdlib: String = libs.stdlib.get().toString()

subprojects {
    version = rootProject.version
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()
    apply<ShadowPlugin>()

    repositories {
        mavenCentral()
    }

    kotlin {
        jvmToolchain(21)
    }

    if (project.name == "core") return@subprojects

    tasks {
        jar { enabled = false }
        build { dependsOn(shadowJar) }
        shadowJar {
            archiveBaseName.set(rootProject.name)
            archiveVersion.set(project.version.toString())
            archiveClassifier.set(project.name)
            destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
            relocate("kotlin", "gg.norisk.libs.kotlin")
            minimize()
        }
    }

    dependencies {
        implementation(project(":core"))
        implementation(stdlib)
    }
}

tasks.jar { enabled = false }