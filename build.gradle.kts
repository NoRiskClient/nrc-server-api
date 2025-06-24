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
        maven("https://repo.papermc.io/repository/maven-public/")
        mavenCentral()
    }

    kotlin {
        jvmToolchain(21)
    }

    tasks {
        jar { enabled = false }
        build { dependsOn(shadowJar) }
        shadowJar {
            archiveBaseName.set(rootProject.name)
            archiveVersion.set(project.version.toString())
            archiveClassifier.set("")
            destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
            manifest {
                attributes("Main-Class" to "gg.norisk.core.NRCServerApi")
            }
        }
    }

    tasks.withType<Wrapper> {
        gradleVersion = "8.12.1"
        distributionType = Wrapper.DistributionType.ALL
    }

    dependencies {
        implementation(stdlib)
        if (project.name != "core") {
            implementation(project(":core"))
        }
    }
}

tasks.jar { enabled = false }