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
            artifactId = "nrc-server-api-fabric"
            version = project.version.toString()
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