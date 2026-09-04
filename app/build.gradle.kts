plugins {
    id("numverify.android.application.compose")
    id("numverify.android.network")
    id("numverify.android.koin")
}

android {
    namespace = "com.phundal.numverify"

    defaultConfig {
        applicationId = "com.phundal.numverify"
        versionCode = 1
        versionName = "1.0"
    }
}

// `apiKey` and `baseUrl` default to the `numverify.apiKey` / `numverify.baseUrl` Gradle
// properties, so they can be overridden per developer in ~/.gradle/gradle.properties or on CI
// without editing a version-controlled file. Uncomment to pin them for this module instead:
//
// numverify {
//     apiKey = "..."
//     baseUrl = "https://apilayer.net/api/"
// }
