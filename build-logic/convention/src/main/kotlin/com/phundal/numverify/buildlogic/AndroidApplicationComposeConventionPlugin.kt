package com.phundal.numverify.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * The Android application module, rendering Compose UI.
 *
 * Applies [AndroidApplicationConventionPlugin] first so the order in which the module lists the
 * plugin ids does not matter.
 */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(AndroidApplicationConventionPlugin::class.java)

        configureCompose(extensions.getByType<ApplicationExtension>())
    }
}
