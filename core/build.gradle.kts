plugins {
    kotlin("jvm")
    id("java-library")
}

dependencies {
    implementation(libs.stdlib)
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "gg.norisk.core.ServerApi"
        )
    }
}