package com.phundal.numverify.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.BuildConfigField
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies

/**
 * Shared configuration for the Android application module: SDK levels, Java/Kotlin compatibility,
 * build types, and the generated `BuildConfig` fields.
 *
 * Module *identity* (namespace, applicationId, version) deliberately stays in the module's own
 * build script; only build *policy* lives here.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("com.android.application")

        val numverify = extensions.create<NumverifyExtension>("numverify").apply {
            apiKey.convention(providers.gradleProperty(API_KEY_PROPERTY))
            baseUrl.convention(providers.gradleProperty(BASE_URL_PROPERTY))
        }

        extensions.configure<ApplicationExtension> {
            compileSdk = AndroidSdk.COMPILE_SDK

            defaultConfig {
                minSdk = AndroidSdk.MIN_SDK
                targetSdk = AndroidSdk.TARGET_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            buildFeatures {
                buildConfig = true
            }
        }

        configureBuildConfigFields(numverify)

        dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)

            testImplementation(libs.junit)
            androidTestImplementation(libs.androidx.junit)
            androidTestImplementation(libs.androidx.espresso.core)
        }
    }

    /**
     * Contributes `BuildConfig` fields through the variant API rather than `defaultConfig`, so the
     * values stay lazy providers all the way to task execution. Nothing is read eagerly at
     * configuration time and no `afterEvaluate` hook is needed.
     *
     * `orElse("")` rather than a throwing fallback provider is deliberate: the configuration cache
     * evaluates `providers.provider { }` when it stores the entry, so a throwing fallback would
     * fire even when the value *is* configured. Validation lives in [BuildConfigValues] instead,
     * inside the `map`, which only runs when the value is actually needed.
     */
    private fun Project.configureBuildConfigFields(numverify: NumverifyExtension) {
        val apiKey = numverify.apiKey.orElse("").map(BuildConfigValues::requireApiKey)
        val baseUrl = numverify.baseUrl.orElse("").map(BuildConfigValues::normalizeBaseUrl)

        extensions.configure<ApplicationAndroidComponentsExtension> {
            onVariants { variant ->
                variant.buildConfigFields?.apply {
                    put(
                        "API_KEY",
                        apiKey.map {
                            BuildConfigField(
                                "String",
                                BuildConfigValues.stringLiteral(it),
                                "NumVerify API access key.",
                            )
                        },
                    )
                    put(
                        "BASE_URL",
                        baseUrl.map {
                            BuildConfigField(
                                "String",
                                BuildConfigValues.stringLiteral(it),
                                "NumVerify API base URL.",
                            )
                        },
                    )
                }
            }
        }
    }
}
