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

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = ".debug"
            resValue("string", "app_version", "${defaultConfig.versionName}${versionNameSuffix}")
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "app_version", "${defaultConfig.versionName}")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
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
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.recyclerview)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.moshi.converter)
    implementation(libs.retrofit.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)

    kapt(libs.hilt.android.compiler)
    kapt(libs.retrofit.moshi.codegen)
    kapt(libs.room.compiler)

    testImplementation(libs.junit)
}

fun <T : Any> propOrDef(propertyName: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    val propertyValue = project.properties[propertyName] as T?
    return propertyValue ?: defaultValue
}