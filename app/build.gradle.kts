plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.onagitube.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.onagitube"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.coil)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.material)
    implementation(libs.timber)
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.process)
    implementation(libs.lifecycle.common)

    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":downloader"))
    implementation(project(":extractor-api"))
    implementation(project(":extractor-builtin"))
}
