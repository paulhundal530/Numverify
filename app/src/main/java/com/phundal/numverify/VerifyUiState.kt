package com.phundal.numverify

import com.phundal.numverify.data.Countries
import com.phundal.numverify.data.NumberValidation

data class VerifyUiState(
    val isLoading: Boolean = true,
    val countries: Countries? = null,
    val verification: NumberValidation? = null,
    val error: String? = null
)