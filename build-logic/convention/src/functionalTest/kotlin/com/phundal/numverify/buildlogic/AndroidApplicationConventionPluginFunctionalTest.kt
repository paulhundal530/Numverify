package com.phundal.numverify.buildlogic

import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * TestKit coverage for the convention plugins.
 *
 * These build a real (tiny) Android project and assert on the resulting build, which is the only
 * way to verify the properties the project actually cares about: that the plugins configure AGP
 * correctly, that the build is configuration-cache reusable, and that it does not trip any
 * isolated-projects violation.
 */
class AndroidApplicationConventionPluginFunctionalTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var project: TestProject

    private val appBuildScript = """
        plugins {
            id("numverify.android.application.compose")
            id("numverify.android.network")
            id("numverify.android.koin")
        }

        android {
            namespace = "com.example.functional"

            defaultConfig {
                applicationId = "com.example.functional"
                versionCode = 1
                versionName = "1.0"
            }
        }
    """

    @Before
    fun setUp() {
        assumeNotNull(TestProject.androidSdkDir)
        project = TestProject(temporaryFolder.root)
            .settings()
            .rootBuildScript()
            .gradleProperties(
                "numverify.apiKey" to "functional-test-key",
                "numverify.baseUrl" to "https://example.test/api",
            )
            .appBuildScript(appBuildScript)
    }

    @Test
    fun `configures the android application`() {
        val result = project.runner(":app:tasks").build()

        assertTrue(
            "expected AGP tasks in the task list:\n${result.output}",
            result.output.contains("installDebug") && result.output.contains("signingReport"),
        )
    }

    @Test
    fun `generates build config fields from gradle properties`() {
        val result = project.runner(":app:generateDebugBuildConfig").build()

        assertEquals(
            TaskOutcome.SUCCESS,
            result.task(":app:generateDebugBuildConfig")!!.outcome,
        )
        val buildConfig = project.generatedBuildConfig()
        assertTrue(buildConfig, buildConfig.contains("""API_KEY = "functional-test-key""""))
        // The base URL in gradle.properties has no trailing slash; the plugin adds one.
        assertTrue(buildConfig, buildConfig.contains("""BASE_URL = "https://example.test/api/""""))
    }

    @Test
    fun `configuration cache entry is reused`() {
        project.runner(":app:generateDebugBuildConfig", "--configuration-cache").build()

        val second = project.runner(":app:generateDebugBuildConfig", "--configuration-cache").build()

        assertTrue(
            "expected the configuration cache entry to be reused:\n${second.output}",
            second.output.contains("Configuration cache entry reused"),
        )
    }

    @Test
    fun `changing the api key property invalidates the configuration cache`() {
        project.runner(":app:generateDebugBuildConfig", "--configuration-cache").build()

        val result = project
            .runner(
                ":app:generateDebugBuildConfig",
                "--configuration-cache",
                "-Pnumverify.apiKey=rotated-key",
            )
            .build()

        assertTrue(
            "expected the entry to be invalidated by the changed property:\n${result.output}",
            result.output.contains("Configuration cache entry stored") ||
                result.output.contains("Calculating task graph as configuration cache cannot be reused"),
        )
        assertTrue(project.generatedBuildConfig().contains("""API_KEY = "rotated-key""""))
    }

    @Test
    fun `builds without isolated projects violations`() {
        val result = project
            .runner(
                ":app:generateDebugBuildConfig",
                "-Dorg.gradle.unsafe.isolated-projects=true",
            )
            .build()

        assertEquals(
            TaskOutcome.SUCCESS,
            result.task(":app:generateDebugBuildConfig")!!.outcome,
        )
    }
}
