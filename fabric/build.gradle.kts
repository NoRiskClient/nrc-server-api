plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin.jvm)
    id("java-library")
}

dependencies {
    minecraft(libs.mojang.minecraft)
    mappings(libs.fabric.yarn)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.language.kotlin)
    implementation(project(":core"))
    implementation(libs.stdlib)
}

tasks {
    jar {
        enabled = true
        archiveBaseName.set("${rootProject.name}-fabric")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
        manifest {
            attributes("Main-Class" to "gg.norisk.fabric.Fabric")
        }
    }
    remapJar {
        dependsOn(jar)
        input.set(jar.get().archiveFile)
    }
}