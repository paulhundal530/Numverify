package com.phundal.numverify.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.phundal.numverify.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface RetrofitProvider {
    fun get(): Retrofit

    companion object {
        fun create(client: OkHttpClient): RetrofitProvider =
            DefaultRetrofitProvider(client)
    }
}

internal class DefaultRetrofitProvider(
    private val okHttpClient: OkHttpClient
) : RetrofitProvider {
    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }
    override fun get(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

}