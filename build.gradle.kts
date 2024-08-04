import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

defaultTasks("shadowJar")

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "mc.play"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-snapshots"
    }
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.11.0")
    implementation("com.intellectualsites.http:HTTP4J:1.6-SNAPSHOT")
    implementation("com.jeff-media:custom-block-data:2.2.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.register<Copy>("copyToServer") {
    from(tasks.named("shadowJar").get().outputs)
    into("D:\\Minecraft Servers\\PlayMC\\plugins")

    // rely on the shadowJar task to build the jar
    dependsOn("shadowJar")
}

tasks.withType<ShadowJar> {
    minimize()
}