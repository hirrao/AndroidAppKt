plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val getTag = {
    try {
        ProcessBuilder("git", "describe", "--tags").start().inputStream.bufferedReader().readText()
            .trim().ifEmpty { "v0.0.0" }
    } catch (e: Exception) {
        "v0.0.0"
    }
}

val parseVersionName = {
    val tag = getTag()
    tag.removePrefix("v")
}

val parseVersionCode = {
    val tag = getTag()
    val versionParts = tag.removePrefix("v").split(".")
    if (versionParts.size == 3) {
        versionParts[0].toInt() * 10000 + versionParts[1].toInt() * 100 + versionParts[2].toInt()
    } else {
        100000
    }
}
android {
    namespace = "com.hirrao.appktp"
    defaultConfig {
        applicationId = "com.hirrao.appktp"
        minSdk = 26
        targetSdk = 35
        versionCode = parseVersionCode()
        versionName = parseVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    compileSdk = 35
    buildToolsVersion = "35.0.1"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}