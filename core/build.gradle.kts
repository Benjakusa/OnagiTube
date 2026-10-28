plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ktlint")
    id("detekt") version("1.21.0")
}

repositories {
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    kotlin("reflect")
    kotlin("serialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
}