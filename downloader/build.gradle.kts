plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ktlint")
}

repositories {
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    kotlin("reflect")
    kotlin("serialization")
    implementation(project(":extractor-api"))
    implementation("com.squareup.okhttp:okhttp:4.12.0")
}