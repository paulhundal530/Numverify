package com.phundal.numverify.feature.numverify.api

/**
 * The credentials and endpoint this feature talks to.
 *
 * Supplied by `:app`, which is the only module with a `BuildConfig`. Taking it as a value rather
 * than reading it from a generated class keeps the feature buildable — and testable — outside the
 * application module.
 */
data class NumverifyConfiguration(
    val apiKey: String,
    val baseUrl: String,
)
