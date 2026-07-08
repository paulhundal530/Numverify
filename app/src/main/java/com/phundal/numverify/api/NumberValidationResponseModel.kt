package com.phundal.numverify.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NumberValidationResponseModel(
    val valid: Boolean,
    val number: String,
    @SerialName("local_format") val localFormat: String = "",
    @SerialName("international_format") val internationalFormat: String = "",
    @SerialName("country_prefix") val countryPrefix: String = "",
    @SerialName("country_code") val countryCode: String = "",
    val location: String = "",
    val carrier: String = "",
    @SerialName("line_type") val lineType: String = ""
)
