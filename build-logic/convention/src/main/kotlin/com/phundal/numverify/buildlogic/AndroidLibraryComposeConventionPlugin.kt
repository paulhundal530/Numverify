package com.phundal.numverify.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * An Android library module that renders Compose UI — a feature's `ui` module, or a shared piece
 * of the design system.
 *
 * Applies [AndroidLibraryConventionPlugin] first so the order in which the module lists the plugin
 * ids does not matter.
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(AndroidLibraryConventionPlugin::class.java)

        configureCompose(extensions.getByType<LibraryExtension>())
    }
}
