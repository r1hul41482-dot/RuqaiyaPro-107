plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.ruqaiyapro"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.ruqaiyapro"
        minSdk = 24
        targetSdk = 34
        versionCode = 107
        versionName = "107-FINAL-BossRubel"
    }
    buildTypes { release { isMinifyEnabled = false } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.4.8" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")

    // CAMERA X - Eita missing chilo!
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")

    // MLKIT FACE DETECTION - Eita missing chilo!
    implementation("com.google.mlkit:face-detection:16.1.7")
    implementation("com.google.mlkit:object-detection:17.0.1")
    implementation("com.google.android.gms:play-services-mlkit-face-detection:17.1.0")

    // WORK MANAGER + OTHERS (tor worker folder er jonno)
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("coil-compose:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
}
