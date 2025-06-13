import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
}

val parseVersionName = {
    try {
        ProcessBuilder(
            "git", "describe", "--tags", "--abbrev=0"
        ).start().inputStream.bufferedReader().readText().trim().ifEmpty {
            logger.error("ERROR in GET TAGS")
            "v1.0.0"
        }
    } catch (_: Exception) {
        logger.error("ERROR in GET TAGS")
        "v1.0.0"
    }.removePrefix("v")
}

val parseVersionCode = {
    try {
        ProcessBuilder(
            "git", "rev-list", "--count", "HEAD"
        ).start().inputStream.bufferedReader().readText().trim().ifEmpty {
            logger.error("ERROR in GET TAGS")
            "1"
        }
    } catch (_: Exception) {
        logger.error("ERROR in GET TAGS")
        "1"
    }.toInt()
}

android {
    namespace = "com.hirrao.appktp2"
    defaultConfig {
        applicationId = "com.hirrao.appktp2"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        compileSdk = libs.versions.compileSdk.get().toInt()
        buildToolsVersion = "35.0.1"
        versionCode = parseVersionCode()
        versionName = parseVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val keyStore = rootProject.file("signature.properties")
        if (keyStore.exists()) {
            create("release") {
                val prop = Properties().apply {
                    keyStore.inputStream().use(this::load)
                }
                storeFile = rootProject.file("release.jks")
                storePassword = prop.getProperty("signing.storePassword")
                keyAlias = prop.getProperty("signing.keyAlias")
                keyPassword = prop.getProperty("signing.keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.findByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            versionNameSuffix = "-debug"
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
}

dependencies {
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
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