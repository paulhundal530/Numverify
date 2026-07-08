package com.phundal.numverify.data

import com.phundal.numverify.api.CountryInformationResponseModel

data class CountryInformation(
    val countryName: String,
    val diallingCode: String
)

data class Countries(
    val countries: Map<String, CountryInformation>
)

fun CountryInformationResponseModel.toCountryInformation(): CountryInformation {
    return CountryInformation(
        countryName = countryName,
        diallingCode = diallingCode
    )
}

fun Map<String, CountryInformationResponseModel>.toCountries(): Countries {
    return Countries(
        countries = mapValues { it.value.toCountryInformation() }
    )
}