pluginManagement {
    // The convention plugins that configure this build live in their own build. Including it
    // here makes `numverify.*` plugin ids resolvable from every project without a
    // `buildscript { classpath(...) }` block.
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Numverify"

include(":app")

// Shared building blocks. Anything usable by more than one feature belongs here.
include(":common:navigation")
include(":common:networking")

// Features. Each is an api/impl/ui triad: `api` is the JVM-only contract other modules compile
// against, `impl` holds implementations nothing else may see, and `ui` renders the screens.
include(":features:numverify:numverify-api")
include(":features:numverify:numverify-impl")
include(":features:numverify:numverify-ui")
