plugins {
    kotlin("jvm")
    id("java-library")
}

dependencies {
    implementation(libs.paper)
    implementation(project(":core"))
}

tasks {
    jar { enabled = false }
    build { dependsOn(shadowJar) }
    shadowJar {
        archiveBaseName.set("${rootProject.name}-paper")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
        manifest {
            attributes(
                "Main-Class" to "gg.norisk.paper.NRCServerApiPlugin"
            )
        }
    }
}