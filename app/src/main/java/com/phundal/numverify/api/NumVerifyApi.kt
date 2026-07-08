package com.phundal.numverify.api

import com.phundal.numverify.BuildConfig
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface NumVerifyApi {

    @GET("countries")
    suspend fun getCountries(
        @Query("access_key") accessKey: String = BuildConfig.API_KEY
    ): Map<String, CountryInformationResponseModel>

    @GET("validate")
    suspend fun validate(
        @Query("access_key") accessKey: String = BuildConfig.API_KEY,
        @Query("number") number: String,
        @Query("country_code") countryCode: String? = null,
        @Query("format") format: Int = 1,
        @Query("callback") callback: String? = null
    ): NumberValidationResponseModel

    companion object {
        fun create(retrofit: Retrofit): NumVerifyApi =
            retrofit.create(NumVerifyApi::class.java)
    }
}
