package com.phundal.numverify.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

/**
 * The `libs` catalog of the *consuming* build.
 *
 * Precompiled/binary plugins do not get the generated `libs` accessors, so the catalog is looked
 * up by name. This reads only the current project's own extensions, which keeps it compatible
 * with isolated projects.
 */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/** Looks up [alias], failing with an actionable message instead of a bare `NoSuchElementException`. */
internal fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).orElseThrow {
        IllegalArgumentException(
            "No library alias `$alias` in the `libs` version catalog. Add it to gradle/libs.versions.toml."
        )
    }
