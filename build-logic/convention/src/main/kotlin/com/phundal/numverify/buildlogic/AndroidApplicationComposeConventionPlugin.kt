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

        dependencies {
            val bom = platform(libs.androidx.compose.bom)

            implementation(bom)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.ui.graphics)
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.compose.material3)

            debugImplementation(libs.androidx.compose.ui.tooling)
            debugImplementation(libs.androidx.compose.ui.test.manifest)

            androidTestImplementation(bom)
            androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        }
    }
}
