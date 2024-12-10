plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
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
            keyAlias = "feast-debug"
            keyPassword = "feast-debug"
            storeFile = rootProject.file("release/app-debug.keystore")
            storePassword = "feast-debug"
        }
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
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.app.compat)
    implementation(libs.coil.runtime)
    implementation(libs.constraint.layout)
    implementation(libs.core.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.data.store)
    implementation(libs.hilt.android)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.material)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.recyclerview)

    kapt(libs.hilt.android.compiler)

    testImplementation(libs.junit)
}

fun <T : Any> propOrDef(propertyName: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    val propertyValue = project.properties[propertyName] as T?
    return propertyValue ?: defaultValue
}