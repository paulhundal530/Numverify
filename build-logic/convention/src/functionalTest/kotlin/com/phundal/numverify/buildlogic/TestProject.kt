package com.phundal.numverify.buildlogic

import java.io.File
import java.util.Properties
import org.gradle.testkit.runner.GradleRunner

/**
 * A throwaway build used to exercise the convention plugins end to end via TestKit.
 *
 * The generated build is wired up exactly like the real one — `pluginManagement { includeBuild }`
 * plus the plugins declared `apply false` in the root script — rather than through
 * `GradleRunner.withPluginClasspath()`. Injecting the plugin classpath puts AGP on a different
 * class loader than the one the root project sees, which AGP's own version check rejects, and it
 * would not exercise the composition the real build actually uses.
 *
 * The `build-logic` sources and the version catalog are copied into a fixture directory that is
 * shared by all tests in the JVM, so the convention plugins are compiled once rather than once per
 * test, while each test still gets a pristine project (and therefore a pristine configuration
 * cache) of its own.
 */
internal class TestProject(val root: File) {

    /** Declares the build and the modules it contains. Call before adding module sources. */
    fun settings(vararg modulePaths: String): TestProject = apply {
        val includes = modulePaths.joinToString("\n") { """include("$it")""" }
        File(root, "settings.gradle.kts").writeText(
            """
            pluginManagement {
                includeBuild("${File(fixture, "build-logic").invariantSeparatorsPath}")
                repositories {
                    google()
                    mavenCentral()
                    gradlePluginPortal()
                }
            }
            dependencyResolutionManagement {
                repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
                repositories {
                    google()
                    mavenCentral()
                }
                versionCatalogs {
                    create("libs") {
                        from(files("${File(fixture, "gradle/libs.versions.toml").invariantSeparatorsPath}"))
                    }
                }
            }
            rootProject.name = "numverify-functional-test"
            $includes
            """.trimIndent()
        )
    }

    fun gradleProperties(vararg entries: Pair<String, String>): TestProject = apply {
        File(root, "gradle.properties").writeText(
            buildString {
                appendLine("org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8")
                entries.forEach { (key, value) -> appendLine("$key=$value") }
            }
        )
        File(root, "local.properties")
            .writeText("sdk.dir=${androidSdkDir!!.invariantSeparatorsPath}")
    }

    /** Mirrors the real root build script: the plugins the convention plugins apply, `apply false`. */
    fun rootBuildScript(): TestProject = apply {
        File(root, "build.gradle.kts").writeText(
            """
            plugins {
                alias(libs.plugins.android.application) apply false
                alias(libs.plugins.android.library) apply false
                alias(libs.plugins.kotlin.jvm) apply false
                alias(libs.plugins.kotlin.compose) apply false
                alias(libs.plugins.kotlin.serialization) apply false
            }
            """.trimIndent()
        )
    }

    /** An Android module: build script, a minimal manifest, and an empty ProGuard file. */
    fun androidModule(path: String, buildScript: String, source: String? = null): TestProject =
        apply {
            val dir = moduleDir(path)
            File(dir, "build.gradle.kts").writeText(buildScript.trimIndent())
            File(dir, "proguard-rules.pro").writeText("")
            File(dir, "src/main").mkdirs()
            File(dir, "src/main/AndroidManifest.xml").writeText(
                """
                |<?xml version="1.0" encoding="utf-8"?>
                |<manifest xmlns:android="http://schemas.android.com/apk/res/android" />
                """.trimMargin()
            )
            source?.let { writeSource(dir, it) }
        }

    /** A Kotlin/JVM module: build script and nothing Android-shaped at all. */
    fun jvmModule(path: String, buildScript: String, source: String? = null): TestProject = apply {
        val dir = moduleDir(path)
        File(dir, "build.gradle.kts").writeText(buildScript.trimIndent())
        source?.let { writeSource(dir, it) }
    }

    fun runner(vararg arguments: String): GradleRunner = GradleRunner.create()
        .withProjectDir(root)
        .withTestKitDir(testKitDir)
        .withArguments(*arguments, "--stacktrace")

    fun moduleDir(path: String): File =
        File(root, path.removePrefix(":").replace(':', '/')).apply { mkdirs() }

    fun generatedBuildConfig(module: String = ":app"): String = File(
        moduleDir(module),
        "build/generated/source/buildConfig/debug/com/example/functional/BuildConfig.java",
    ).readText()

    private fun writeSource(moduleDir: File, source: String) {
        File(moduleDir, "src/main/kotlin/com/example/functional").apply { mkdirs() }
            .resolve("Source.kt")
            .writeText(source.trimIndent())
    }

    companion object {

        /** Absolute path to the repository's version catalog, injected by the `functionalTest` task. */
        private val versionCatalog: File = File(
            requireNotNull(System.getProperty("numverify.versionCatalog")) {
                "System property `numverify.versionCatalog` is not set; check the functionalTest task."
            }
        )

        private val repositoryRoot: File = versionCatalog.parentFile.parentFile

        /** A stable TestKit home so dependencies and the fixture survive between runs. */
        val testKitDir: File = File(
            requireNotNull(System.getProperty("numverify.testkit.gradleUserHome")),
            "testkit",
        )

        /**
         * A copy of `build-logic` and the version catalog, refreshed once per JVM. Copying rather
         * than including the real directory keeps the tests from contending with the outer build
         * for locks on `build-logic/**/build`; leaving the copy's own build directory in place
         * keeps the compilation incremental across runs.
         */
        private val fixture: File by lazy {
            File(testKitDir.parentFile, "functional-fixture").apply {
                mkdirs()
                repositoryRoot.resolve("gradle/libs.versions.toml")
                    .copyTo(resolve("gradle/libs.versions.toml"), overwrite = true)
                copySources(
                    from = repositoryRoot.resolve("build-logic"),
                    to = resolve("build-logic"),
                )
            }
        }

        /** Copies build scripts and sources, leaving generated directories in either tree alone. */
        private fun copySources(from: File, to: File) {
            val ignored = setOf("build", ".gradle", ".kotlin")
            from.walkTopDown()
                .onEnter { it.name !in ignored }
                .filter(File::isFile)
                .forEach { source ->
                    source.copyTo(to.resolve(source.relativeTo(from)), overwrite = true)
                }
        }

        /**
         * The Android SDK, if one is available. AGP cannot be configured without it, so the tests
         * are skipped rather than failed on machines that have no SDK installed.
         */
        val androidSdkDir: File? by lazy {
            val fromEnvironment = sequenceOf("ANDROID_HOME", "ANDROID_SDK_ROOT")
                .mapNotNull(System::getenv)
                .filter(String::isNotBlank)
            val fromLocalProperties = sequenceOf(repositoryRoot.resolve("local.properties"))
                .filter(File::isFile)
                .mapNotNull { file ->
                    Properties().apply { file.inputStream().use(::load) }.getProperty("sdk.dir")
                }
            (fromEnvironment + fromLocalProperties).map(::File).firstOrNull(File::isDirectory)
        }
    }
}
