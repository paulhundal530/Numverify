package com.phundal.numverify.feature.numverify.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CountryInformationResponseModel(
    @SerialName("country_name") val countryName: String,
    @SerialName("dialling_code") val diallingCode: String,
)

@Serializable
internal data class NumberValidationResponseModel(
    val valid: Boolean,
    val number: String,
    @SerialName("local_format") val localFormat: String = "",
    @SerialName("international_format") val internationalFormat: String = "",
    @SerialName("country_prefix") val countryPrefix: String = "",
    @SerialName("country_code") val countryCode: String = "",
    val location: String = "",
    val carrier: String = "",
    @SerialName("line_type") val lineType: String = "",
)
