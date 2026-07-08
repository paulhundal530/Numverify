package com.phundal.numverify.data

import com.phundal.numverify.api.NumVerifyApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface CountriesRepository {
    suspend fun getCountriesResponse(): Result<Countries>
}

internal class DefaultCountriesRepository(
    private val api: NumVerifyApi,
    private val dispatcher: CoroutineDispatcher,
) : CountriesRepository {

    override suspend fun getCountriesResponse(): Result<Countries> = withContext(dispatcher) {
        try {
            val countries = api.getCountries()
            Result.success(countries.toCountries())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}