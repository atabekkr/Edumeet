plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.safeargs.kotlin)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.imax.edumeet"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.imax.edumeet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "OPEN_AI_KEY",
            "\"${project.findProperty("OPEN_AI_KEY")}\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //room
    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)

    //fragment and navigation
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //reflection-free flavor by kirich
    implementation(libs.viewbinding.kirich)

    //Retrofit
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    //dagger-hilt
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.gson)

    implementation(libs.dots.indicator)

    //Youtube video player
    implementation(libs.youtube.player)

    implementation(libs.glide)
    implementation(libs.audio.recorder)

    // Ktor for Android networking
    implementation("io.ktor:ktor-client-android:2.3.12")

    // Ktor core
    implementation("io.ktor:ktor-client-core:2.3.12")

    // Content negotiation for serializing JSON
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")

    // Ktor logging for debugging HTTP requests
    implementation("io.ktor:ktor-client-logging:2.3.12")

    // Kotlin serialization for handling JSON data
    implementation("io.ktor:ktor-client-serialization:2.3.12")

}