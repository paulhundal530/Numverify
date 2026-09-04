package com.phundal.numverify.feature.numverify.impl

import com.phundal.numverify.common.networking.RetrofitFactory
import com.phundal.numverify.feature.numverify.api.CountriesRepository
import com.phundal.numverify.feature.numverify.api.NumberValidationRepository
import com.phundal.numverify.feature.numverify.api.NumverifyConfiguration
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * The NumVerify feature's implementation bindings.
 *
 * This — and the `numverify-api` types it satisfies — is the whole public surface of this module.
 * Install it from `:app` alongside [com.phundal.numverify.common.networking.networkingModule] and
 * a [NumverifyConfiguration].
 */
val numverifyModule = module {

    single<NumVerifyApi> {
        get<RetrofitFactory>()
            .create(get<NumverifyConfiguration>().baseUrl)
            .create(NumVerifyApi::class.java)
    }

    single<CountriesRepository> {
        DefaultCountriesRepository(
            api = get(),
            configuration = get(),
            dispatcher = Dispatchers.IO,
        )
    }

    single<NumberValidationRepository> {
        DefaultNumberValidationRepository(
            api = get(),
            configuration = get(),
            dispatcher = Dispatchers.IO,
        )
    }
}
