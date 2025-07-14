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
    apply<MavenPublishPlugin>()

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

        processResources {
            val props = mapOf("version" to project.version)
            inputs.properties(props)
            filteringCharset = "UTF-8"
            filesMatching(listOf("**/*.yml", "**/*.yaml", "**/*.json")) {
                expand(props)
            }
        }
    }

    dependencies {
        if (project.name != "core") {
            implementation(project(":core"))
        }
        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")
        //slf4j
        implementation("org.slf4j:slf4j-api:2.0.9")
        implementation("org.slf4j:slf4j-simple:2.0.9")
    }

    publishing {
        publications {
            create<MavenPublication>("binaryAndSources") {
                groupId = project.group.toString()
                artifactId = "server-api-${project.name}"
                version = project.version.toString()
            }
        }

        repositories {
            fun MavenArtifactRepository.applyCredentials() = credentials {
                username = (System.getenv("NORISK_NEXUS_USERNAME") ?: project.findProperty("noriskMavenUsername")).toString()
                password = (System.getenv("NORISK_NEXUS_PASSWORD") ?: project.findProperty("noriskMavenPassword")).toString()
            }
            maven {
                name = "production"
                url = uri("https://maven.norisk.gg/repository/norisk-production/")
                applyCredentials()
            }
            maven {
                name = "dev"
                url = uri("https://maven.norisk.gg/repository/maven-releases/")
                applyCredentials()
            }
        }
    }
}
