package com.phundal.numverify.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * The Compose-aware half of Koin: `koinViewModel()`, `koinInject()` and friends.
 *
 * Split from [KoinConventionPlugin] so that a module which only declares bindings — a feature's
 * `impl`, or `:common:networking` — does not drag the Compose integration onto its classpath.
 */
class KoinComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(KoinConventionPlugin::class.java)

        dependencies {
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}
