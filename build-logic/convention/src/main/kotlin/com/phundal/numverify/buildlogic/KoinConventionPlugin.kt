package com.phundal.numverify.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** The Koin dependency-injection stack for Android modules, aligned through the Koin BOM. */
class KoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        dependencies {
            implementation(platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.android)
        }
    }
}
