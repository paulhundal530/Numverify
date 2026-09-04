package com.phundal.numverify.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Turns on Jetpack Compose for the application module and pins every Compose artifact through the
 * Compose BOM.
 *
 * Applies [AndroidApplicationConventionPlugin] first so the order in which the module lists the
 * plugin ids does not matter.
 */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(AndroidApplicationConventionPlugin::class.java)
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.configure<ApplicationExtension> {
            buildFeatures {
                compose = true
            }
        }

        val bom = libs.library("androidx-compose-bom")
        dependencies {
            add("implementation", platform(bom))
            add("implementation", libs.library("androidx-activity-compose"))
            add("implementation", libs.library("androidx-compose-ui"))
            add("implementation", libs.library("androidx-compose-ui-graphics"))
            add("implementation", libs.library("androidx-compose-ui-tooling-preview"))
            add("implementation", libs.library("androidx-compose-material3"))

            add("debugImplementation", libs.library("androidx-compose-ui-tooling"))
            add("debugImplementation", libs.library("androidx-compose-ui-test-manifest"))

            add("androidTestImplementation", platform(bom))
            add("androidTestImplementation", libs.library("androidx-compose-ui-test-junit4"))
        }
    }
}
