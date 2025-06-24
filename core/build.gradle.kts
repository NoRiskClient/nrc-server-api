plugins {
    kotlin("jvm")
    id("java-library")
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "gg.norisk.core.NRCServerApi"
        )
    }
}