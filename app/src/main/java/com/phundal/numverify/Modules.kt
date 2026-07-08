package com.phundal.numverify

import com.phundal.numverify.api.NumVerifyApi
import com.phundal.numverify.api.OkHttpClientProvider
import com.phundal.numverify.api.RetrofitProvider
import com.phundal.numverify.data.CountriesRepository
import com.phundal.numverify.data.DefaultCountriesRepository
import com.phundal.numverify.data.DefaultNumberValidationRepository
import com.phundal.numverify.data.NumberValidationRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single<OkHttpClient> { OkHttpClientProvider.create().create() }

    single<Retrofit> { RetrofitProvider.create(client = get()).get() }

    single<NumVerifyApi> { NumVerifyApi.create(retrofit = get()) }

    single<CountriesRepository> {
        DefaultCountriesRepository(
            api = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<NumberValidationRepository> {
        DefaultNumberValidationRepository(
            api = get(),
            dispatcher = Dispatchers.IO
        )
    }

    viewModel {
        VerificationViewModel(
            countriesRepository = get(),
            validationRepository = get()
        )
    }
}