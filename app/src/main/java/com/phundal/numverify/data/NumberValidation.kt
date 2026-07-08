package com.phundal.numverify.data

import com.phundal.numverify.api.NumberValidationResponseModel

data class NumberValidation(
    val valid: Boolean,
    val number: String,
    val localFormat: String,
    val internationalFormat: String,
    val countryPrefix: String,
    val countryCode: String,
    val location: String,
    val carrier: String,
    val lineType: String
)

fun NumberValidationResponseModel.toNumberValidation(): NumberValidation {
    return NumberValidation(
        valid = valid,
        number = number,
        localFormat = localFormat,
        internationalFormat = internationalFormat,
        countryPrefix = countryPrefix,
        countryCode = countryCode,
        location = location,
        carrier = carrier,
        lineType = lineType
    )
}