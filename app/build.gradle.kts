import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.dagger.plugin)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.rach.lawyerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rach.lawyerapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String","WEB_CLIENT_KEY","\"${getApiKey("WEB_CLIENT_KEY")}\"")
        buildConfigField("String","RAZORPAY_ID","\"${getApiKey("RAZORPAY_ID")}\"")

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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.play.services.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.material.icons.extended)
    implementation(libs.navigation)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.complier)
    implementation(libs.hilt.navagtionCompose)
    implementation(libs.coil)
    implementation(libs.material)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.razorpay:checkout:1.6.41")
    implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+")
    implementation("com.guolindev.permissionx:permissionx:1.8.0")
}

fun getApiKey(key:String):String{
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()){
        localProperties.load(FileInputStream(localPropertiesFile))
        return localProperties[key]?.toString() ?: ""
    }
    return ""
}