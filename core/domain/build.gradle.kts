plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
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
    create("staging") {
      initWith(getByName("release"))
    }

    create("benchmark") {
      initWith(getByName("debug"))
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
  kotlinOptions {
    jvmTarget = "21"
  }

  kotlin {
    compilerOptions {
      freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
  }
}

dependencies {
  implementation(libs.coroutines.core)
  implementation("javax.inject:javax.inject:1")
  implementation(libs.kotlinx.datetime)
  implementation(libs.paging.runtime)
}
