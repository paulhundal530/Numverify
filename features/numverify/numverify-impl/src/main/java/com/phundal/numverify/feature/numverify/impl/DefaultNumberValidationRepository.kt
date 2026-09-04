package com.phundal.numverify.feature.numverify.impl

import com.phundal.numverify.feature.numverify.api.NumberValidation
import com.phundal.numverify.feature.numverify.api.NumberValidationRepository
import com.phundal.numverify.feature.numverify.api.NumverifyConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultNumberValidationRepository(
    private val api: NumVerifyApi,
    private val configuration: NumverifyConfiguration,
    private val dispatcher: CoroutineDispatcher,
) : NumberValidationRepository {

    override suspend fun validate(
        number: String,
        countryCode: String,
        callback: String?,
    ): Result<NumberValidation> = withContext(dispatcher) {
        try {
            Result.success(
                api.validate(
                    accessKey = configuration.apiKey,
                    number = number,
                    countryCode = countryCode,
                    callback = callback,
                ).toNumberValidation()
            )
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
