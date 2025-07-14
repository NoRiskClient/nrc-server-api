import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaLibraryPlugin

plugins {
    id("idea")
    id("java")
    id("maven-publish")
}

allprojects {
    group = "gg.norisk"
    version = "0.1.0"
}

repositories {
    mavenCentral()
    maven {
        name = "NoriskMaven"
        url = uri("https://maven.norisk.gg/releases")
        credentials {
            username = project.findProperty("mavenUsername") as String? ?: System.getenv("MAVEN_USERNAME")
            password = project.findProperty("mavenPassword") as String? ?: System.getenv("MAVEN_PASSWORD")
        }
    }
}

tasks.jar {
    enabled = false
}

subprojects {
    version = rootProject.version
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()

    val mavenUser = project.findProperty("mavenUsername") as String? ?: System.getenv("MAVEN_USERNAME")
    val mavenPass = project.findProperty("mavenPassword") as String? ?: System.getenv("MAVEN_PASSWORD")
    if (mavenUser.isNullOrBlank() || mavenPass.isNullOrBlank()) {
        throw GradleException("Maven Repository Credentials fehlen! Bitte mavenUsername/mavenPassword in gradle.properties oder MAVEN_USERNAME/MAVEN_PASSWORD als Umgebungsvariablen setzen.")
    }

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven(uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots"))
        mavenCentral()
        maven {
            name = "NoriskMaven"
            url = uri("https://maven.norisk.gg/releases")
            credentials {
                username = mavenUser
                password = mavenPass
            }
        }
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
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                groupId = "gg.norisk"
                artifactId = "server-api-${project.name}"
                version = rootProject.version.toString()
                
                pom {
                    name.set("NoRisk Server API ${project.name.capitalize()}")
                    description.set("${project.name.capitalize()} plugin API for NoRisk server")
                    url.set("https://github.com/norisk/noriskclient-server-api")
                    licenses {
                        license {
                            name.set("GNU Lesser General Public License v3.0")
                            url.set("https://www.gnu.org/licenses/lgpl-3.0.html")
                        }
                    }
                    developers {
                        developer {
                            id.set("S42")
                            name.set("S42")
                        }
                    }
                }
            }
        }
        repositories {
            mavenLocal()
            maven {
                name = "NoriskMaven"
                url = uri("https://maven.norisk.gg/releases")
                credentials {
                    username = mavenUser
                    password = mavenPass
                }
            }
        }
    }
}