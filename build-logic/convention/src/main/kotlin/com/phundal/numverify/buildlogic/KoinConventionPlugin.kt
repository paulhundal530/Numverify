package com.phundal.numverify.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** The Koin dependency-injection stack, aligned through the Koin BOM. */
class KoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        dependencies {
            add("implementation", platform(libs.library("koin-bom")))
            add("implementation", libs.library("koin-core"))
            add("implementation", libs.library("koin-android"))
            add("implementation", libs.library("koin-androidx-compose"))
            add("implementation", libs.library("koin-compose"))
            add("implementation", libs.library("koin-compose-viewmodel"))
        }
    }
}
