plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.myapplication34"
    compileSdk = 35
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.myapplication34"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        // Add this line to access the API key from gradle.properties
        buildConfigField("String", "GEMINI_API_KEY", "\"${project.findProperty("GEMINI_API_KEY")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    dependencies {
        implementation ("com.google.android.material:material:1.9.0")
        implementation ("androidx.appcompat:appcompat:1.6.1")
            implementation ("androidx.cardview:cardview:1.0.0")
        implementation ("androidx.fragment:fragment:1.4.0")
            implementation ("androidx.recyclerview:recyclerview:1.3.1") // Add this line
            // Other dependencies
        // Use the correct version (0.3.0 instead of 0.3.1)
        implementation("com.google.ai.client.generativeai:generativeai:0.3.0")

            implementation ("com.squareup.retrofit2:retrofit:2.9.0")
            implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation ("androidx.viewpager2:viewpager2:1.0.0")

        // Required Guava version
        implementation("com.google.guava:guava:32.0.0-android")
// Correct initialization
        implementation ("com.squareup.retrofit2:retrofit:2.9.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    }
}