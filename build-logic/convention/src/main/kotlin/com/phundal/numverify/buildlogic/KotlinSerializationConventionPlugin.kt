package com.phundal.numverify.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Applies the kotlinx.serialization compiler plugin together with its JSON runtime. */
class KotlinSerializationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
