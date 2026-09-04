plugins {
    id("numverify.android.application.compose")
    id("numverify.android.koin.compose")
}

android {
    namespace = "com.phundal.numverify"

    defaultConfig {
        applicationId = "com.phundal.numverify"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // The glue layer: `:app` installs the modules and picks the start destination, and that is all
    // it knows. It depends on `numverify-impl` for its bindings only — no implementation type is
    // ever named here, which is why nothing breaks when the feature's internals change.
    implementation(project(":common:navigation"))
    implementation(project(":common:networking"))
    implementation(project(":features:numverify:numverify-api"))
    implementation(project(":features:numverify:numverify-impl"))
    implementation(project(":features:numverify:numverify-ui"))
}

// `apiKey` and `baseUrl` default to the `numverify.apiKey` / `numverify.baseUrl` Gradle
// properties, so they can be overridden per developer in ~/.gradle/gradle.properties or on CI
// without editing a version-controlled file. Uncomment to pin them for this module instead:
//
// numverify {
//     apiKey = "..."
//     baseUrl = "https://apilayer.net/api/"
// }
