import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.application")
  id("kotlin-android")
  id("com.google.devtools.ksp")
  id("dagger.hilt.android.plugin")
  id("androidx.navigation.safeargs.kotlin")
}

kotlin {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
    optIn.add("kotlin.time.ExperimentalTime")
  }
}
//configure<com.android.build.api.dsl.ApplicationExtension> {
android {
  namespace = "com.ak.feastit"
  compileSdk = libs.versions.compileSDK.get().toInt()

  defaultConfig {
    namespace = "com.ak.feastit"
    applicationId = "com.ak.feastit"
    minSdk = libs.versions.minSDK.get().toInt()
    targetSdk = libs.versions.targetSDK.get().toInt()
    versionCode = libs.versions.versionCode.get().toInt()
    versionName = libs.versions.versionName.get()
    buildConfigField("String", "API_KEY", "\"" + propOrDef("SPOONACULAR_KEY", "") + "\"")

    testInstrumentationRunner = "com.ak.feastit.HiltTestRunner"
  }

  signingConfigs {
    getByName("debug") {
      storeFile = rootProject.file("release/app-debug.jks")
      storePassword = "foodie"
      keyAlias = "androidDebug"
      keyPassword = "foodie"
    }

    create("release") {
      if (rootProject.file("release/app-release.jks").exists()) {
        storeFile = rootProject.file("release/app-release.jks")
        storePassword = properties["FEAST_IT_STORE_PWD"]?.toString() ?: ""
        keyAlias = "feast-it"
        keyPassword = properties["FEAST_IT_KEY_PWD"]?.toString() ?: ""
      }
    }
  }

  buildTypes {
    getByName("debug") {
      signingConfig = signingConfigs["debug"]
      isMinifyEnabled = false
      isShrinkResources = false
      applicationIdSuffix = ".debug"
      versionNameSuffix = ".debug"
      resValue("string", "app_version", "${defaultConfig.versionName}$versionNameSuffix")
    }

    getByName("release") {
      signingConfig = signingConfigs["release"] ?: signingConfigs["debug"]
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      resValue("string", "app_version", "${defaultConfig.versionName}")
    }

    create("staging") {
      initWith(getByName("release"))
      signingConfig = signingConfigs.findByName("debug")
      applicationIdSuffix = ".staging"
      versionNameSuffix = ".staging"
      matchingFallbacks.add("release")
    }

    create("benchmark") {
      initWith(getByName("release"))
      signingConfig = signingConfigs.findByName("debug")
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "dontobfuscate.pro")
      matchingFallbacks.add("release")
    }
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  testOptions {
    unitTests.isReturnDefaultValues = true
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

dependencies {
//    todo need to have feature based modularization
  implementation(project(":core:domain"))
  implementation(project(":core:database"))
  implementation(project(":core:remote"))
  implementation(project(":core:data"))
  implementation(project(":core:domain"))
  implementation(project(":core:media"))
  implementation(libs.hilt.common)
  implementation(libs.hilt.work)
  debugImplementation(libs.leakcanary.android)
  implementation(libs.app.compat)
  implementation(libs.fragment.ktx)
  implementation(libs.coil)
  implementation(libs.coil.network.okhttp)
  implementation(libs.constraint.layout)
  implementation(libs.core.ktx)
  implementation(libs.coroutines.android)
  implementation(libs.coroutines.core)
  implementation(libs.data.store)
  implementation(libs.hilt.android)
  implementation(libs.kotlinx.datetime)
  implementation(libs.lifecycle.viewmodel.ktx)
  implementation(libs.material)
  implementation(libs.navigation.fragment.ktx)
  implementation(libs.navigation.ui.ktx)
  implementation(libs.recyclerview)
  implementation(libs.work.runtime.ktx)
  implementation(libs.timber)
  implementation(libs.slf4j.api)
  implementation(libs.logback.android)
  implementation(libs.paging.runtime)
  implementation(libs.preference.ktx)

  implementation(libs.androidx.profileinstaller)

  ksp(libs.hilt.android.compiler)
  kspAndroidTest(libs.hilt.android.compiler)
  testImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.test.core)
  androidTestImplementation(libs.test.runner)
  androidTestImplementation(libs.test.rules)
  androidTestImplementation(libs.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
  androidTestImplementation(libs.espresso.contrib)
  androidTestImplementation(libs.hilt.android.testing)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  debugImplementation(libs.fragment.testing)
  debugImplementation(libs.test.core)

  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.junit.jupiter.params)
  // This is the actual test engine that discovers and runs tests written with JUnit Jupiter.
  testRuntimeOnly(libs.junit.jupiter.engine)
  // This component is responsible for launching the test execution process. It discovers test
  // engines (like junit-jupiter-engine) on the classpath and delegates test discovery and
  // execution to them
  testRuntimeOnly(libs.junit.platform.launcher)

  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
}

fun <T : Any> propOrDef(
  propertyName: String,
  defaultValue: T,
): T {
  @Suppress("UNCHECKED_CAST")
  val propertyValue = project.properties[propertyName] as T?
  return propertyValue ?: defaultValue
}
