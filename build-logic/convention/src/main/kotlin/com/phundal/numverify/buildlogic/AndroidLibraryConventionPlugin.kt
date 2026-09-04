package com.phundal.numverify.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Shared configuration for Android library modules — the `common` building blocks and the `impl`
 * and `ui` halves of a feature.
 *
 * Deliberately narrower than the application plugin: no `targetSdk` (a property of the shipped
 * app, not of a library) and no `BuildConfig` (one generated class per module is noise, and a
 * library that needs a build-time constant should be handed it by `:app` instead).
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("com.android.library")

        extensions.configure<LibraryExtension> {
            configureAndroidCommon()

            defaultConfig.testInstrumentationRunner = ANDROID_TEST_RUNNER

            buildFeatures.buildConfig = false
        }

        dependencies {
            implementation(platform(libs.kotlinx.coroutines.bom))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.android)

            testImplementation(libs.junit)
            androidTestImplementation(libs.androidx.junit)
        }
    }
}
