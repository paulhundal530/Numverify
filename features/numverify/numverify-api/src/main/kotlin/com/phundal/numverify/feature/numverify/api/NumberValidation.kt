package com.phundal.numverify.feature.numverify.api

/** The outcome of validating a single phone number. */
data class NumberValidation(
    val valid: Boolean,
    val number: String,
    val localFormat: String,
    val internationalFormat: String,
    val countryPrefix: String,
    val countryCode: String,
    val location: String,
    val carrier: String,
    val lineType: String,
)
