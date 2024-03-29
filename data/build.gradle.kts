plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
}

android {
    compileSdk = Dependencies.android.compileSdk
    buildToolsVersion = Dependencies.android.buildTools

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources.pickFirsts.add("META-INF/LICENSE.txt")
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
}

dependencies {
    implementation(project(":domain"))
    room()
    retrofit()
    mail()
}

fun DependencyHandlerScope.room() {
    api(Dependencies.room.runtime)
    kapt(Dependencies.room.compiler)
    api(Dependencies.room.ktx)
}

fun DependencyHandlerScope.retrofit() {
    api(Dependencies.retrofit.retrofit2)
    api(Dependencies.retrofit.converterGson)
    api(Dependencies.retrofit.interceptor)
    api(Dependencies.retrofit.retrofitCoroutines)
}

fun DependencyHandlerScope.mail() {
    api(Dependencies.api.javaMail)
    api(Dependencies.api.javaMailActivation)
}


