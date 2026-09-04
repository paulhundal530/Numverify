package com.phundal.numverify.buildlogic

import org.gradle.api.provider.Property

/**
 * `numverify { }` DSL contributed by [AndroidApplicationConventionPlugin].
 *
 * Both values default to the corresponding Gradle properties ([API_KEY_PROPERTY] and
 * [BASE_URL_PROPERTY]), read through the provider API so that the configuration cache tracks them
 * as build inputs and invalidates the cached entry when they change.
 */
abstract class NumverifyExtension {

    /** The NumVerify API access key emitted as `BuildConfig.API_KEY`. */
    abstract val apiKey: Property<String>

    /** The NumVerify API base URL emitted as `BuildConfig.BASE_URL`. */
    abstract val baseUrl: Property<String>
}
