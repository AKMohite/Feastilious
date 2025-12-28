plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("dagger.hilt.android.plugin")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.mak.feastit.database"
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
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
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
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
  implementation(libs.room.ktx)
  implementation(libs.room.paging)
  implementation(libs.room.runtime)
  ksp(libs.room.compiler)
  implementation(libs.kotlinx.datetime)
  implementation(libs.paging.runtime)
}
