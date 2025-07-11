// Top-level build file where you can add configuration options common to all sub-projects/modules.
apply(plugin = "com.github.ben-manes.versions")
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.plugin)
        classpath(libs.hilt.plugin)
        classpath(libs.navigation.safe.args.plugin)
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.0-1.0.29")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.42.0")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:7.1.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register("copyGitHooks", Copy::class.java) {
  description = "Copies the git hooks from /git-hooks to the .git folder."
  group = "git hooks"
  from("$rootDir/scripts/pre-commit")
  into("$rootDir/.git/hooks/")
}

tasks.register("installGitHooks", Exec::class.java) {
  description = "Installs the pre-commit git hooks from /git-hooks."
  group = "git hooks"
  workingDir = rootDir
  commandLine = listOf("chmod")
  args("-R", "+x", ".git/hooks/")
  dependsOn("copyGitHooks")
  doLast {
    logger.info("Git hook installed successfully.")
  }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")

            ktlint()
            licenseHeaderFile(rootProject.file("spotless/feast-copyright.txt"))
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    checkForGradleUpdate = true
    outputFormatter = "html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "dependency-updates-report"
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
