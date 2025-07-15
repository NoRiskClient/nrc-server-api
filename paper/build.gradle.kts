plugins {
    id("java-library")
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.paper)
    implementation(project(":core"))
}

tasks {
    jar { enabled = false }
    shadowJar {
        dependsOn(":jar")
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
        if (!names.contains("binaryAndSources")) {
            create<MavenPublication>("binaryAndSources") {
                groupId = project.group.toString()
                artifactId = "paper"
                version = project.version.toString()
                artifact(tasks["shadowJar"])
                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])
            }
        }
    }
}