// app/build.gradle.kts (Nivel de Módulo app)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.victor.sigeva"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.victor.sigeva"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    // Gson para manejar la respuesta de la API
    implementation("com.google.code.gson:gson:2.13.1") // Considera agregar esto a tu libs.versions.toml también

    implementation(libs.androidx.constraintlayout)

    // Volley para hacer las peticiones de la API
    implementation("com.android.volley:volley:1.2.1") // Considera agregar esto a tu libs.versions.toml también

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
