plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.bibleapp.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {



    api(project(":feature:book"))
    api(project(":feature:chapter"))
    api(project(":feature:verse"))
    api(project(":feature:versecontent"))

    api("androidx.core:core-ktx:1.9.0")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")


    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    api("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    api("androidx.activity:activity-compose:1.6.1")
    api("androidx.compose.ui:ui:1.3.3")
    api("androidx.compose.ui:ui-tooling-preview:1.3.3")
    api("androidx.compose.material:material:1.3.1")
    api("androidx.compose.material:material-icons-extended:1.3.1")
    api("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestApi("androidx.test.espresso:espresso-core:3.5.1")
    androidTestApi("androidx.compose.ui:ui-test-junit4:1.3.3")
    debugApi("androidx.compose.ui:ui-tooling:1.3.3")
    debugApi("androidx.compose.ui:ui-test-manifest:1.3.3")

    //accompanist-systemUiController
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.28.0")

//    // Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//
//    //Moshi
//    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
//
//    // Retrofit with Moshi Converter
//    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
//
//    //animated navigation
//
//
//    //room
//    implementation ("androidx.room:room-runtime:2.5.0")
//    kapt ("androidx.room:room-compiler:2.5.0")
//    implementation ("androidx.room:room-ktx:2.5.0")
//
    // Dagger - Hilt
    api("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    api("androidx.hilt:hilt-navigation-compose:1.0.0")

    //work manager
    implementation("androidx.work:work-runtime-ktx:2.7.1")
}