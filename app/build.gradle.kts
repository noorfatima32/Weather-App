import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.example.weatherapp"
    compileSdk = 35
    buildFeatures.buildConfig = true
    buildFeatures{
    viewBinding = true
}
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }
    val weatherApiKey = localProperties.getProperty("WEATHER_API_KEY") ?: ""
    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "WEATHER_API_KEY", "\"$weatherApiKey\"")
                   }
    buildTypes {
        release {
            buildConfigField("String", "WEATHER_API_KEY", "\"f60ce08d715036053011fb99c05ac87d\"")
        }
        debug {
            buildConfigField("String", "WEATHER_API_KEY", "\"f60ce08d715036053011fb99c05ac87d\"")
        }
    }
    fun getWeatherApiKey(): String {
    return project.findProperty("WEATHER_API_KEY")?.toString() ?: ""
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
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // lottie animation
   implementation("com.airbnb.android:lottie:6.6.2")
    // GSON Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

}