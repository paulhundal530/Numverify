package com.phundal.numverify.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

interface OkHttpClientProvider {
    fun create(): OkHttpClient

    companion object {
        fun create(): OkHttpClientProvider =
            DefaultOkhttpClientProvider()
    }
}

internal class DefaultOkhttpClientProvider : OkHttpClientProvider {
    private val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    override fun create(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

}