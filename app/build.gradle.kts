import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.seiberl.protrackreader"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.seiberl.protrackreader"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("\\config\\keystore.properties")
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

            keyAlias = keystoreProperties["releaseKeyAlias"] as String
            keyPassword = keystoreProperties["releaseKeyPassword"] as String
            val storePath = keystoreProperties["storeFile"] as String
            if (storePath.isNotEmpty()) {
                storeFile = file(storePath)
            }
            storePassword = keystoreProperties["storePassword"] as String
            enableV2Signing = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    configurations {
        create("cleanedAnnotations")
        implementation {
            exclude(group = "com.intellij", module = "annotations")
        }
    }
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
    implementation(libs.androidx.material3.window.size)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.hilt)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    implementation(libs.vico.compose.m3)
    implementation(libs.androidx.adaptive.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    ksp(libs.hilt.compiler)
    ksp(libs.androidx.room.compiler)
}