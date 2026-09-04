package com.phundal.numverify.common.networking

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

/**
 * The shared networking bindings.
 *
 * Install this once from `:app`; every feature that needs to talk to a service injects
 * [RetrofitFactory] and builds its own service interface from it.
 */
val networkingModule = module {

    single {
        Json {
            // Server responses routinely carry fields the client has no model for; ignoring them
            // keeps an additive API change from becoming a crash.
            ignoreUnknownKeys = true
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()
    }

    single<RetrofitFactory> { DefaultRetrofitFactory(client = get(), json = get()) }
}
