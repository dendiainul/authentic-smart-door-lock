plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.authentic.smartdoor.camera"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    val roomVersion = "2.6.1"
    val retrofitVersion = "2.11.0"
    val okhttpVersion = "4.12.0"

    // Use storage module for Room
    implementation(project(":storage"))

    // Retrofit core
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

    // Gson converter untuk JSON
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // OkHttp untuk logging dan interceptor
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    // Coroutines support untuk suspend functions
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")

    // google sign in
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.runtime)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.foundation)
    implementation("androidx.compose.material3:material3")
    implementation(libs.androidx.ui.text)
    testImplementation(libs.junit)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.ui.tooling)
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-rtsp:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")

}