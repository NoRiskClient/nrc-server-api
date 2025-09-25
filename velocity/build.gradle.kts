plugins {
    id("java-library")
    alias(libs.plugins.shadow)
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

dependencies {
    compileOnly(libs.velocity)
    implementation(project(":core"))
    annotationProcessor(libs.velocity)
}

tasks {
    jar { enabled = true }
    shadowJar {
        dependsOn(jar)
        archiveBaseName.set("${rootProject.name}-velocity")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build { dependsOn(shadowJar) }
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
                artifactId = "velocity"
                version = project.version.toString()
                from(components["java"])
                artifact(tasks["shadowJar"])
                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])
            }
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "nrc-server-api-velocity"
            version = project.version.toString()
            artifact(tasks["shadowJar"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
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
