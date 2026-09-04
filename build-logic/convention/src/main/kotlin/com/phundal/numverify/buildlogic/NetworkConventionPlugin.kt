package com.phundal.numverify.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * The project's networking stack: Retrofit over OkHttp, with responses decoded by
 * kotlinx.serialization. OkHttp artifacts are aligned through its BOM.
 */
class NetworkConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(KotlinSerializationConventionPlugin::class.java)

        dependencies {
            add("implementation", platform(libs.library("okhttp-bom")))
            add("implementation", libs.library("okhttp"))
            add("implementation", libs.library("logging-interceptor"))
            add("implementation", libs.library("retrofit"))
            add("implementation", libs.library("retrofit2-kotlinx-serialization-converter"))
        }
    }
}
