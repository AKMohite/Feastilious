plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.coroutines.core)
    implementation("javax.inject:javax.inject:1")
    implementation(libs.kotlinx.datetime)

}