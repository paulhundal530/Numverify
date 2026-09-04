package com.phundal.numverify.feature.numverify.impl

import com.phundal.numverify.feature.numverify.api.Countries
import com.phundal.numverify.feature.numverify.api.CountriesRepository
import com.phundal.numverify.feature.numverify.api.NumverifyConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultCountriesRepository(
    private val api: NumVerifyApi,
    private val configuration: NumverifyConfiguration,
    private val dispatcher: CoroutineDispatcher,
) : CountriesRepository {

    override suspend fun getCountriesResponse(): Result<Countries> = withContext(dispatcher) {
        try {
            Result.success(api.getCountries(accessKey = configuration.apiKey).toCountries())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
