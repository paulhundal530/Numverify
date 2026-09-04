package com.phundal.numverify.feature.numverify.ui

import com.phundal.numverify.feature.numverify.api.Countries
import com.phundal.numverify.feature.numverify.api.NumberValidation

internal data class VerifyUiState(
    val isLoading: Boolean = true,
    val countries: Countries? = null,
    val verification: NumberValidation? = null,
    val error: String? = null,
)
