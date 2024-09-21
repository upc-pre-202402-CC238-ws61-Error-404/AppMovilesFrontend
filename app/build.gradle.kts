plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.chaquitaclla_appmovil_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chaquitaclla_appmovil_android"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_URL", "\"http://10.0.2.2:5138/api/v1/\"")
        buildConfigField("String", "BEARER_TOKEN", "\"your-bearer-token\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", "\"https://your-release-api-url.com/\"")
            buildConfigField("String", "BEARER_TOKEN", "\"your-release-bearer-token\"")
        }
        debug {
            buildConfigField("String", "API_URL", "\"https://your-debug-api-url.com/\"")
            buildConfigField("String", "BEARER_TOKEN", "\"your-debug-bearer-token\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures.buildConfig = true
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}