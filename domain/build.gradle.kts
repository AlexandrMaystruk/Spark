plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    di()
}

fun DependencyHandlerScope.di() {
    api(Dependencies.di.javaxInject)
}