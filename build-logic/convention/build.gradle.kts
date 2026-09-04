plugins {
    `kotlin-dsl`
}

group = "com.phundal.numverify.buildlogic"

// Location of the classes Gradle generated for this build's `libs` catalog. `libs` here is an
// instance of a decorated subclass, so the accessor class itself is the superclass.
val versionCatalogAccessors: File =
    File(libs.javaClass.superclass.protectionDomain.codeSource.location.toURI())

// The daemon runs on JDK 21 (see gradle/gradle-daemon-jvm.properties), so compiling the
// convention plugins against the same toolchain keeps the produced classes loadable.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// A separate source set for TestKit tests keeps the fast, pure unit tests in `test` from being
// slowed down by full Gradle builds.
val functionalTest: SourceSet = sourceSets.create("functionalTest")

configurations.named(functionalTest.implementationConfigurationName) {
    extendsFrom(configurations.testImplementation.get())
}
configurations.named(functionalTest.runtimeOnlyConfigurationName) {
    extendsFrom(configurations.testRuntimeOnly.get())
}

dependencies {
    // The Gradle plugins these convention plugins configure are `compileOnly`: they are supplied
    // at runtime by the consuming build, which declares them in its root build script with
    // `apply false`. This avoids leaking two copies of AGP onto the build classpath.
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    // Gradle generates the type-safe `libs` accessors (org.gradle.accessors.dm.LibrariesForLibs)
    // for build *scripts* only, so plugin source code normally has to look aliases up by string.
    // Compiling against the generated classes gives the convention plugins the same
    // `libs.androidx.core.ktx` accessors a build script gets, so a typo or a removed alias is a
    // compile error here rather than a failure in the app build. `compileOnly` is deliberate: the
    // consuming build generates the very same class from the very same TOML (build-logic reads
    // ../gradle/libs.versions.toml) and supplies it at runtime.
    compileOnly(files(versionCatalogAccessors))

    testImplementation(libs.junit)

    "functionalTestImplementation"(gradleTestKit())
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "numverify.android.application"
            implementationClass =
                "com.phundal.numverify.buildlogic.AndroidApplicationConventionPlugin"
            description = "Applies the shared Android application configuration."
        }
        register("androidApplicationCompose") {
            id = "numverify.android.application.compose"
            implementationClass =
                "com.phundal.numverify.buildlogic.AndroidApplicationComposeConventionPlugin"
            description = "The application module, rendering Compose UI."
        }
        register("androidLibrary") {
            id = "numverify.android.library"
            implementationClass =
                "com.phundal.numverify.buildlogic.AndroidLibraryConventionPlugin"
            description = "Applies the shared Android library configuration."
        }
        register("androidLibraryCompose") {
            id = "numverify.android.library.compose"
            implementationClass =
                "com.phundal.numverify.buildlogic.AndroidLibraryComposeConventionPlugin"
            description = "An Android library module that renders Compose UI."
        }
        register("jvmLibrary") {
            id = "numverify.jvm.library"
            implementationClass = "com.phundal.numverify.buildlogic.JvmLibraryConventionPlugin"
            description = "A plain Kotlin/JVM module, used by the feature `api` contracts."
        }
        register("kotlinSerialization") {
            id = "numverify.kotlin.serialization"
            implementationClass =
                "com.phundal.numverify.buildlogic.KotlinSerializationConventionPlugin"
            description = "Applies kotlinx.serialization and its JSON runtime."
        }
        register("androidNetwork") {
            id = "numverify.android.network"
            implementationClass = "com.phundal.numverify.buildlogic.NetworkConventionPlugin"
            description = "Adds the Retrofit/OkHttp networking stack."
        }
        register("androidKoin") {
            id = "numverify.android.koin"
            implementationClass = "com.phundal.numverify.buildlogic.KoinConventionPlugin"
            description = "Adds the Koin dependency injection stack."
        }
        register("androidKoinCompose") {
            id = "numverify.android.koin.compose"
            implementationClass = "com.phundal.numverify.buildlogic.KoinComposeConventionPlugin"
            description = "Adds the Compose-aware Koin integration."
        }
    }
}

val functionalTestTask = tasks.register<Test>("functionalTest") {
    description = "Runs the TestKit tests for the convention plugins."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    testClassesDirs = functionalTest.output.classesDirs
    classpath = functionalTest.runtimeClasspath
    useJUnit()
    // TestKit gets its own Gradle home; parking it under the real one keeps downloaded
    // dependencies between runs instead of re-fetching them into a temporary directory.
    systemProperty("numverify.testkit.gradleUserHome", gradle.gradleUserHomeDir.absolutePath)
    // The generated test builds reuse the repository's real version catalog.
    systemProperty(
        "numverify.versionCatalog",
        layout.settingsDirectory.file("../gradle/libs.versions.toml").asFile.canonicalPath,
    )
}

tasks.named("check") {
    dependsOn(functionalTestTask)
}

