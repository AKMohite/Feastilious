import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

kotlin {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
    optIn.add("kotlin.time.ExperimentalTime")
  }
}

android {
  namespace = "com.mak.feastit.domain"
  compileSdk =
    libs.versions.compileSDK
      .get()
      .toInt()

  defaultConfig {
    minSdk =
      libs.versions.minSDK
        .get()
        .toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
//            isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
}

dependencies {
  implementation(libs.coroutines.core)
  implementation("javax.inject:javax.inject:1")
  implementation(libs.kotlinx.datetime)
  implementation(libs.paging.runtime)
}
