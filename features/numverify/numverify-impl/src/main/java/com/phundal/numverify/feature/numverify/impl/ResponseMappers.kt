package com.phundal.numverify.feature.numverify.impl

import com.phundal.numverify.feature.numverify.api.Countries
import com.phundal.numverify.feature.numverify.api.CountryInformation
import com.phundal.numverify.feature.numverify.api.NumberValidation

/**
 * Translates wire models into the domain models declared by `numverify-api`.
 *
 * This boundary is the point of the api/impl split: the response classes above are free to mirror
 * whatever the service sends, while the rest of the app only ever sees the shapes it needs.
 */
internal fun CountryInformationResponseModel.toCountryInformation(): CountryInformation =
    CountryInformation(
        countryName = countryName,
        diallingCode = diallingCode,
    )

internal fun Map<String, CountryInformationResponseModel>.toCountries(): Countries =
    Countries(countries = mapValues { it.value.toCountryInformation() })

internal fun NumberValidationResponseModel.toNumberValidation(): NumberValidation =
    NumberValidation(
        valid = valid,
        number = number,
        localFormat = localFormat,
        internationalFormat = internationalFormat,
        countryPrefix = countryPrefix,
        countryCode = countryCode,
        location = location,
        carrier = carrier,
        lineType = lineType,
    )
