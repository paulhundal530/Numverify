package com.phundal.numverify.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * A plain Kotlin/JVM module — no Android framework, no resources, no manifest.
 *
 * This is what a feature's `api` module uses. Keeping the contract off the Android platform means
 * it stays compilable (and testable) without an emulator or Robolectric, and it makes accidental
 * dependencies on the Android SDK a compile error rather than a design review comment.
 *
 * Java and Kotlin are pinned to the same bytecode level as the Android modules, so the jars this
 * produces are consumable by them.
 */
class JvmLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = BuildDefaults.JAVA_VERSION
            targetCompatibility = BuildDefaults.JAVA_VERSION
        }

        extensions.configure<KotlinJvmProjectExtension> {
            compilerOptions.jvmTarget.set(BuildDefaults.JVM_TARGET)
        }

        dependencies {
            testImplementation(libs.junit)
        }
    }
}
