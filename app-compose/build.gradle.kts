plugins {
  id("com.android.application")
  id("kotlin-android")
  id("com.google.devtools.ksp")
  id("dagger.hilt.android.plugin")
  id("org.jetbrains.kotlin.plugin.compose")
}

android {
  namespace = "com.ak.feastit.compose"
  compileSdk = libs.versions.compileSDK.get().toInt()

  defaultConfig {
    applicationId = "com.ak.feastit.compose"
    minSdk = libs.versions.minSDK.get().toInt()
    targetSdk = libs.versions.targetSDK.get().toInt()
    versionCode = libs.versions.versionCode.get().toInt()
    versionName = libs.versions.versionName.get()

    testInstrumentationRunner = "com.ak.feastit.compose.HiltTestRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

kotlin {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
  }
}

dependencies {
  implementation(project(":core:domain"))
  implementation(project(":core:database"))
  implementation(project(":core:remote"))
  implementation(project(":core:data"))
  implementation(project(":core:media"))

  implementation(libs.core.ktx)
  implementation(libs.lifecycle.runtime.compose)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material3.windowSizeClass)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.hilt.navigation.compose)

  implementation(libs.hilt.android)
  implementation(libs.hilt.work)
  ksp(libs.hilt.android.compiler)

  implementation(libs.coil.core)
  implementation(libs.coil.compose)
  implementation(libs.coil.network.okhttp)
  implementation(libs.timber)
  implementation(libs.work.runtime.ktx)
  implementation(libs.kotlinx.datetime)
  implementation(libs.paging.runtime)
  implementation(libs.paging.compose)
  implementation(libs.data.store)

  testImplementation(libs.junit.jupiter.api)
  testRuntimeOnly(libs.junit.jupiter.engine)
  testImplementation(libs.mockk)
  testImplementation(libs.kotlinx.coroutines.test)

  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
}
