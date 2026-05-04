plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.seproj"
    compileSdk {
        version = release(36)
    }

    val aiInsightsApiKey = (project.findProperty("GEMINI_API_KEY") as String?) ?: ""
    val aiInsightsModel = (project.findProperty("GEMINI_MODEL") as String?) ?: "gemini-2.5-flash"

    defaultConfig {
        applicationId = "com.example.seproj"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "AI_INSIGHTS_API_KEY", "\"$aiInsightsApiKey\"")
        buildConfigField("String", "AI_INSIGHTS_MODEL", "\"$aiInsightsModel\"")
    }

    buildFeatures {
        buildConfig = true
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
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    testImplementation(libs.junit)

    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.work:work-runtime:2.10.3")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.work:work-testing:2.10.3")
    androidTestImplementation("androidx.work:work-testing:2.10.0")
    implementation("androidx.work:work-runtime:2.10.0")
    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))
    implementation("com.google.firebase:firebase-firestore")
    androidTestImplementation("androidx.work:work-testing:2.10.0")
    androidTestImplementation("androidx.test:core:1.6.1")
    androidTestImplementation("androidx.work:work-testing:2.10.0")
    androidTestImplementation("androidx.test:core:1.6.1")
    androidTestImplementation(libs.ext.junit)
    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test:core:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.work:work-testing:2.10.0")
    androidTestImplementation("org.hamcrest:hamcrest-core:1.3")
    androidTestImplementation("org.hamcrest:hamcrest-library:1.3")
}