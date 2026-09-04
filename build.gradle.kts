// Top-level build file.
//
// The plugins are declared here (and only here) with `apply false` so that AGP and the Kotlin
// compiler plugins land on the shared build classpath. The `numverify.*` convention plugins in the
// `build-logic` included build compile against them as `compileOnly` dependencies and apply them
// to the modules that need them, which is what keeps a single copy of each plugin on the classpath.
//
// Note that nothing here reaches into subprojects (no `allprojects` / `subprojects` blocks) — all
// cross-module configuration goes through the convention plugins, which is what isolated projects
// requires.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// `build-logic` is a separate build, so its tests are not picked up by anything in this build.
// This gives CI (and `./gradlew checkBuildLogic` locally) a single entry point for them.
tasks.register("checkBuildLogic") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs the unit and TestKit tests for the convention plugins in build-logic."
    dependsOn(gradle.includedBuild("build-logic").task(":convention:check"))
}
