dependencies {
    implementation(libs.gson)
}

plugins {
    id("java-library")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "gg.norisk.core.Core"
        )
    }
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

publishing {
    publications {
        if (!names.contains("binaryAndSources")) {
            create<MavenPublication>("binaryAndSources") {
                groupId = project.group.toString()
                artifactId = "core"
                version = project.version.toString()
                from(components["java"])
                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])
            }
        }
    }
}
