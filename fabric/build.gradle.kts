plugins {
    alias(libs.plugins.fabric.loom)
    id("java-library")
}

dependencies {
    minecraft(libs.mojang.minecraft)
    mappings(libs.fabric.yarn)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    implementation(project(":core"))
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