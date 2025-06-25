plugins {
    kotlin("jvm")
    id("java-library")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "gg.norisk.core.Core"
        )
    }
}