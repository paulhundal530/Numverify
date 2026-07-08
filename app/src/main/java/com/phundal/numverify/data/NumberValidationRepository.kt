package com.phundal.numverify.data

import com.phundal.numverify.api.NumVerifyApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface NumberValidationRepository {
    suspend fun validate(
        number: String,
        countryCode: String,
        callback: String? = null
    ): Result<NumberValidation>
}

internal class DefaultNumberValidationRepository(
    private val api: NumVerifyApi,
    private val dispatcher: CoroutineDispatcher,
) : NumberValidationRepository {

    override suspend fun validate(
        number: String,
        countryCode: String,
        callback: String?
    ): Result<NumberValidation> {
        return withContext(dispatcher) {
            try {
                Result.success(
                    api.validate(
                        number = number,
                        countryCode = countryCode,
                        callback = callback
                    ).toNumberValidation()
                )
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }
    }

}