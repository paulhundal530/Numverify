package com.phundal.numverify.buildlogic

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderConvertible

/**
 * The configuration accessors a build script gets for free.
 *
 * Gradle generates `implementation(...)`, `testImplementation(...)` and friends from the
 * configurations a project has, but only for build scripts — plugin code otherwise has to spell the
 * configuration name out as a string in `add("implementation", ...)`. These extensions restore the
 * usual DSL inside a convention plugin's `dependencies { }` block.
 */

internal fun DependencyHandler.implementation(dependency: Any): Unit =
    addDependency("implementation", dependency)

internal fun DependencyHandler.debugImplementation(dependency: Any): Unit =
    addDependency("debugImplementation", dependency)

internal fun DependencyHandler.testImplementation(dependency: Any): Unit =
    addDependency("testImplementation", dependency)

internal fun DependencyHandler.androidTestImplementation(dependency: Any): Unit =
    addDependency("androidTestImplementation", dependency)

/**
 * Routes each notation to the matching `DependencyHandler` method.
 *
 * Catalog accessors come in two shapes: a plain alias such as `libs.retrofit` is a [Provider],
 * while an alias that is also a prefix of other aliases — `libs.androidx.compose.ui`, which
 * `androidx-compose-ui-graphics` nests under — is a [ProviderConvertible]. Both have to stay lazy,
 * so neither may go through the eager `add(String, Any)` overload.
 */
private fun DependencyHandler.addDependency(configuration: String, dependency: Any) {
    when (dependency) {
        is Provider<*> -> addProvider(configuration, dependency)
        is ProviderConvertible<*> -> addProviderConvertible(configuration, dependency)
        else -> add(configuration, dependency)
    }
}
