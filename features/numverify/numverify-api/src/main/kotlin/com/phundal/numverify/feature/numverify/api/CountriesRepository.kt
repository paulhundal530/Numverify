package com.phundal.numverify.feature.numverify.api

/** Reads the countries the service supports. */
interface CountriesRepository {

    suspend fun getCountriesResponse(): Result<Countries>
}
