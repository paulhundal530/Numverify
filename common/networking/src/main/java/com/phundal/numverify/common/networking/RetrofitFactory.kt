package com.phundal.numverify.common.networking

import retrofit2.Retrofit

/**
 * Builds Retrofit instances that share this app's single [okhttp3.OkHttpClient] and JSON setup.
 *
 * Base URLs belong to the feature that calls the service, not to the shared plumbing, so they are
 * passed in rather than configured here. That keeps this module free of any knowledge of which
 * services exist.
 */
interface RetrofitFactory {

    /** A Retrofit bound to [baseUrl], decoding responses with kotlinx.serialization. */
    fun create(baseUrl: String): Retrofit
}
