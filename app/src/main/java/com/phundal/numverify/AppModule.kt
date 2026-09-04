package com.phundal.numverify

import com.phundal.numverify.feature.numverify.api.NumverifyConfiguration
import org.koin.dsl.module

/**
 * The bindings only the application module can provide.
 *
 * `BuildConfig` exists in `:app` alone, so this is where build-time configuration crosses into the
 * feature graph — as a plain value object the feature declared, not as a generated class the
 * feature would have to import.
 */
val appModule = module {

    single {
        NumverifyConfiguration(
            apiKey = BuildConfig.API_KEY,
            baseUrl = BuildConfig.BASE_URL,
        )
    }
}
