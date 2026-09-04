// Settings for the `build-logic` included build.
//
// This build is included via `pluginManagement { includeBuild("build-logic") }` in the root
// settings file, so its published plugin markers are resolvable by every project in the main
// build without any `buildscript { classpath(...) }` declarations.
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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

    // Reuse the main build's version catalog so plugin/library versions live in exactly one place.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"

include(":convention")
