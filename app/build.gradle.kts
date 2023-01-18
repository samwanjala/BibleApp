plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id("kotlin-kapt")
    id("kotlin-parcelize")
//    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.bibleapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.bibleapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    dynamicFeatures += setOf(
        ":feature:book",
        ":feature:chapter",
        ":feature:verse",
        ":feature:versecontent"
    )
}

dependencies {

    api(project(mapOf("path" to ":lib:core")))

    api(project(mapOf("path" to ":lib:data")))
    api(project(mapOf("path" to ":lib:domain")))


}