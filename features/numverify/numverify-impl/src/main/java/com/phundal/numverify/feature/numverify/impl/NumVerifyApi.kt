package com.phundal.numverify.feature.numverify.impl

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The NumVerify HTTP service.
 *
 * `internal`, like everything else in this module: callers work through the repositories declared
 * in `numverify-api`, so the wire format can change without touching another module.
 */
internal interface NumVerifyApi {

    @GET("countries")
    suspend fun getCountries(
        @Query("access_key") accessKey: String,
    ): Map<String, CountryInformationResponseModel>

    @GET("validate")
    suspend fun validate(
        @Query("access_key") accessKey: String,
        @Query("number") number: String,
        @Query("country_code") countryCode: String? = null,
        @Query("format") format: Int = 1,
        @Query("callback") callback: String? = null,
    ): NumberValidationResponseModel
}
