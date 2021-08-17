plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Dependencies.android.compileSdk
    buildToolsVersion = Dependencies.android.buildTools

    defaultConfig {
        applicationId = "com.gmail.maystruks08.spark"
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
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
    buildFeatures {
        viewBinding  = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    core()
    material()
    ui()
    di()
    room()
    async()
    jetpack()
}

fun DependencyHandlerScope.core() {
    implementation(Dependencies.other.kotlin)
    implementation(Dependencies.other.ktxCore)
    implementation(Dependencies.other.kotlinxDatetime)

    implementation(Dependencies.other.appcompat)
    implementation(Dependencies.other.constraintLayout)
}

fun DependencyHandlerScope.material() {
    implementation(Dependencies.other.material)
}

fun DependencyHandlerScope.ui() {
    implementation(Dependencies.image.circleImageView)
}

fun DependencyHandlerScope.room() {
    implementation(Dependencies.room.runtime)
    kapt(Dependencies.room.compiler)
    implementation(Dependencies.room.ktx)
}

fun DependencyHandlerScope.di() {
    implementation(Dependencies.di.dagger2)
    kapt(Dependencies.di.dagger2compiler)
    implementation(Dependencies.di.javaxInject)
}

fun DependencyHandlerScope.retrofit() {
    implementation(Dependencies.retrofit.retrofit2)
    implementation(Dependencies.retrofit.converterGson)
    implementation(Dependencies.retrofit.interceptor)
}

fun DependencyHandlerScope.imageLoading() {
    implementation(Dependencies.image.glide)
    annotationProcessor(Dependencies.image.glideCompiler)
}

fun DependencyHandlerScope.async() {
    implementation(Dependencies.async.coroutinesCore)
    implementation(Dependencies.async.coroutinesAndroid)
}

fun DependencyHandlerScope.jetpack() {
    implementation(Dependencies.jetpack.lifecycleExtensions)
    implementation(Dependencies.jetpack.lifecycleViewModel)
    implementation(Dependencies.jetpack.lifecycleRuntime)
    implementation(Dependencies.jetpack.fragmentX)
    implementation(Dependencies.jetpack.navigationFragment)
    implementation(Dependencies.jetpack.navigationUi)
}

