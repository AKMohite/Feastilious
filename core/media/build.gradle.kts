plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.mak.feastit.media"
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

  buildFeatures {
    viewBinding = true
  }

  buildTypes {
    release {
      isMinifyEnabled = false
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
  kotlinOptions {
    jvmTarget = "21"
  }
}

dependencies {
  implementation(libs.core.ktx)
  implementation(libs.app.compat)
  implementation(libs.material)
//  implementation(libs.activity)
  implementation(libs.constraint.layout)
  implementation(libs.media3.exoplayer)
  implementation(libs.media3.exoplayer.dash)
  implementation(libs.media3.exoplayer.hls)
  implementation(libs.media3.ui)
  implementation(libs.media3.exoplayer.smoothstreaming)
  implementation(libs.media3.exoplayer.rtsp)

  implementation(libs.verticalseekbar)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)
}
