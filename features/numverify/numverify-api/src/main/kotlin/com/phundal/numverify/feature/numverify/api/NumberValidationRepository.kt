package com.phundal.numverify.feature.numverify.api

/** Validates phone numbers against the service. */
interface NumberValidationRepository {

    suspend fun validate(
        number: String,
        countryCode: String,
        callback: String? = null,
    ): Result<NumberValidation>
}
