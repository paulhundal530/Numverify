plugins {
    id("numverify.android.library")
    id("numverify.kotlin.serialization")
    id("numverify.android.koin")
}

android {
    namespace = "com.phundal.numverify.feature.numverify.impl"
}

dependencies {
    // The contract this module implements. `implementation`, not `api`: the only thing this module
    // exposes is a Koin module, so nothing compiles against its types.
    implementation(project(":features:numverify:numverify-api"))

    // Retrofit itself arrives transitively — `:common:networking` exposes it with `api` because it
    // hands out Retrofit instances.
    implementation(project(":common:networking"))
}
