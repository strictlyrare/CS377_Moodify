plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // enable kapt for Moshi code-gen
    kotlin("kapt")
}

android {
    namespace = "javatpoint.app.moodify"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "javatpoint.app.moodify"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["redirectSchemeName"] = "moodify"
        manifestPlaceholders["redirectHostName"]   = "callback"
        buildConfigField(
            "String",
            "SPOTIFY_CLIENT_ID",
            "\"${property("SPOTIFY_CLIENT_ID")}\""
        )
        buildConfigField(
            "String",
            "SPOTIFY_CLIENT_SECRET",
            "\"${property("SPOTIFY_CLIENT_SECRET")}\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Android UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Spotify SDKs
    implementation(libs.auth)   // com.spotify.android:auth:2.1.1
    implementation(
        mapOf(
            "name"    to "spotify-app-remote-release",
            "version" to "0.8.0",
            "ext"     to "aar"
        )
    )

    // Networking & JSON
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.v290)               // retrofit:2.9.0
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)

    // DataStore Preferences (KTX)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // --- Moshi code-gen for your @JsonClass DTOs ---
    implementation("com.squareup.moshi:moshi:1.15.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
}
