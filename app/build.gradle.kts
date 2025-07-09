plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            resValue("string", "app_version", "${defaultConfig.versionName}${versionNameSuffix}")
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
        }
    }

    buildFeatures {
        viewBinding = true
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
//    todo need to have feature based modularization
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:remote"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(libs.hilt.common)
    implementation(libs.hilt.work)
    debugImplementation(libs.leakcanary.android)
    implementation(libs.app.compat)
    implementation(libs.fragment.ktx)
    implementation(libs.coil.runtime)
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

    ksp(libs.hilt.android.compiler)
}

fun <T : Any> propOrDef(propertyName: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    val propertyValue = project.properties[propertyName] as T?
    return propertyValue ?: defaultValue
}