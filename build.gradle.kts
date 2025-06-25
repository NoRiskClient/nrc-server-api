import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaLibraryPlugin

plugins {
    kotlin("jvm") version libs.versions.kotlinPlugin.get()
    id("java")
    id("org.gradle.wrapper")
}

allprojects {
    group = "gg.norisk"
}

repositories {
    mavenCentral()
}

val stdlib: String = libs.stdlib.get().toString()

subprojects {
    version = rootProject.version
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
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
