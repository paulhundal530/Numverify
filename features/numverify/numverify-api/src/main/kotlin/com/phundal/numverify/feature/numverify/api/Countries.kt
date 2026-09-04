package com.phundal.numverify.feature.numverify.api

/** A country the service can validate numbers for. */
data class CountryInformation(
    val countryName: String,
    val diallingCode: String,
)

/** The supported countries, keyed by ISO country code. */
data class Countries(
    val countries: Map<String, CountryInformation>,
)
