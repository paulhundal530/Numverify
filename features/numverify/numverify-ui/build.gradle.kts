plugins {
    id("numverify.android.library.compose")
    id("numverify.android.koin.compose")
}

android {
    namespace = "com.phundal.numverify.feature.numverify.ui"
}

dependencies {
    // `api`, because NumverifyFeatureEntry is a FeatureEntry and the host consumes it as one.
    api(project(":common:navigation"))

    // The contract this UI renders. Note the absence of `numverify-impl`: the screen is compiled
    // against interfaces only, and dependency injection supplies the implementations at runtime.
    implementation(project(":features:numverify:numverify-api"))
}
