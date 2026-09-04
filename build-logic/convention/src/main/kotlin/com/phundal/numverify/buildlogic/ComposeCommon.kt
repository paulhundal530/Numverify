package com.phundal.numverify.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Turns on Jetpack Compose and pins every Compose artifact through the Compose BOM.
 *
 * Shared by the application and library Compose convention plugins so that a screen behaves the
 * same whether it is hosted by `:app` or by a feature's `ui` module.
 */
internal fun Project.configureCompose(extension: CommonExtension) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    extension.buildFeatures.compose = true

    dependencies {
        val bom = platform(libs.androidx.compose.bom)

        implementation(bom)
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)
        implementation(libs.androidx.lifecycle.runtime.compose)
        implementation(libs.androidx.lifecycle.viewmodel.compose)

        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)

        androidTestImplementation(bom)
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    }
}
