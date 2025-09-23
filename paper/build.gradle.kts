plugins {
    id("java-library")
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.paper)
    implementation(project(":core"))
}

tasks {
    jar { enabled = true } // JAR-Task aktivieren
    shadowJar {
        dependsOn(jar)
        archiveBaseName.set("${rootProject.name}-paper")
        archiveVersion.set(version.toString())
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
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "nrc-server-api-paper"
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