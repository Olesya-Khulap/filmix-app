import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.kotlin.kapt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.filmix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.filmix"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        println("=== DEBUG INFO ===")
        println("Local properties file path: ${localPropertiesFile.absolutePath}")
        println("Local properties file exists: ${localPropertiesFile.exists()}")

        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
            println("Properties loaded, size: ${localProperties.size}")
            println("All properties:")
            localProperties.forEach { key, value ->
                println("  $key = $value")
            }
        }

        val apiKey = localProperties.getProperty("TMDB_API_KEY") ?: "YOUR_KEY_HERE"
        println("Final API key: $apiKey")
        println("API key length: ${apiKey.length}")
        println("==================")

        buildConfigField("String", "TMDB_API_KEY", "\"$apiKey\"")
        val properties = Properties()
        val secretsFile = project.rootProject.file("secrets.properties")
        if (secretsFile.exists()) {
            properties.load(secretsFile.inputStream())
            buildConfigField("String", "TMDB_BEARER_TOKEN", "\"${properties["TMDB_BEARER_TOKEN"]}\"")
        } else {
            buildConfigField("String", "TMDB_BEARER_TOKEN", "\"\"")
        }
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Fragment KTX
    implementation(libs.androidx.fragment.ktx)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // ViewModel and LiveData (MVVM)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // UI Components
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.recyclerview)
    implementation(libs.flexbox)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.core.splashscreen)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Image Loading
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.51.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
