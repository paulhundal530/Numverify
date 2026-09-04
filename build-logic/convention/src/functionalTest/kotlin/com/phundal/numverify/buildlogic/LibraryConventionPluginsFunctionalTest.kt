package com.phundal.numverify.buildlogic

import java.util.zip.ZipFile
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * TestKit coverage for the module types the app is split into: a JVM-only contract module and an
 * Android library that renders Compose and consumes that contract.
 *
 * Together these assert the property the module graph relies on — that a feature's `api` module
 * really is buildable without the Android SDK, and that its jar is consumable from an Android
 * library compiled at the same bytecode level.
 */
class LibraryConventionPluginsFunctionalTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var project: TestProject

    @Before
    fun setUp() {
        assumeNotNull(TestProject.androidSdkDir)
        project = TestProject(temporaryFolder.root)
            .settings(":contract", ":widget")
            .rootBuildScript()
            .gradleProperties()
            .jvmModule(
                path = ":contract",
                buildScript = """
                    plugins {
                        id("numverify.jvm.library")
                    }
                """,
                source = """
                    package com.example.functional

                    data class Contract(val route: String)
                """,
            )
            .androidModule(
                path = ":widget",
                buildScript = """
                    plugins {
                        id("numverify.android.library.compose")
                        id("numverify.android.koin.compose")
                    }

                    android {
                        namespace = "com.example.functional.widget"
                    }

                    dependencies {
                        implementation(project(":contract"))
                    }
                """,
                source = """
                    package com.example.functional

                    import androidx.compose.material3.Text
                    import androidx.compose.runtime.Composable

                    @Composable
                    fun Widget(contract: Contract) {
                        Text(contract.route)
                    }
                """,
            )
    }

    @Test
    fun `jvm library builds a jar without the android plugin`() {
        val result = project.runner(":contract:jar").build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":contract:jar")!!.outcome)

        val jar = project.moduleDir(":contract").resolve("build/libs").listFiles()
            ?.single { it.extension == "jar" }
        val entries = ZipFile(checkNotNull(jar) { "no jar produced" }).use { zip ->
            zip.entries().toList().map { it.name }
        }
        assertTrue(entries.toString(), entries.contains("com/example/functional/Contract.class"))
    }

    @Test
    fun `jvm library has no android tasks`() {
        val result = project.runner(":contract:tasks").build()

        assertFalse(
            "a JVM-only module must not gain Android tasks:\n${result.output}",
            result.output.contains("installDebug") || result.output.contains("signingReport"),
        )
    }

    @Test
    fun `android library compiles compose and consumes the jvm contract`() {
        val result = project.runner(":widget:assembleDebug").build()

        assertEquals(
            TaskOutcome.SUCCESS,
            result.task(":widget:compileDebugKotlin")!!.outcome,
        )
    }

    /**
     * Library modules opt out of `BuildConfig`; only the application module generates one. A stray
     * `BuildConfig` per library is dead weight in the graph, and a library that reaches for a
     * build-time constant should be handed it by `:app` instead.
     */
    @Test
    fun `android library generates no build config`() {
        val result = project.runner(":widget:assembleDebug").build()

        assertEquals(null, result.task(":widget:generateDebugBuildConfig"))
        assertFalse(project.moduleDir(":widget").resolve("build/generated/source/buildConfig").exists())
    }
}
