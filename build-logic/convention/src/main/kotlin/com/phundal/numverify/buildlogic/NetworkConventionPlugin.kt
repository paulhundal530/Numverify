package com.phundal.numverify.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * The project's networking stack: Retrofit over OkHttp, with responses decoded by
 * kotlinx.serialization. OkHttp artifacts are aligned through its BOM.
 *
 * Retrofit is exposed with `api` because a module applying this plugin hands `Retrofit` instances
 * to its consumers; OkHttp and the converter stay `implementation` because they are wiring the
 * consumer never names.
 */
class NetworkConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply(KotlinSerializationConventionPlugin::class.java)

        dependencies {
            api(libs.retrofit)

            implementation(platform(libs.okhttp.bom))
            implementation(libs.okhttp)
            implementation(libs.logging.interceptor)
            implementation(libs.retrofit2.kotlinx.serialization.converter)
        }
    }
}
