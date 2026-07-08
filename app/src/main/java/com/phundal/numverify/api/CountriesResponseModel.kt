package com.phundal.numverify.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryInformationResponseModel(
    @SerialName("country_name") val countryName: String,
    @SerialName("dialling_code") val diallingCode: String
)
