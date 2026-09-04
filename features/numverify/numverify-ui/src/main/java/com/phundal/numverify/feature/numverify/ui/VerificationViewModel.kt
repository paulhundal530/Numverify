package com.phundal.numverify.feature.numverify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phundal.numverify.feature.numverify.api.CountriesRepository
import com.phundal.numverify.feature.numverify.api.NumberValidationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class VerificationViewModel(
    private val countriesRepository: CountriesRepository,
    private val validationRepository: NumberValidationRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<VerifyUiState> = MutableStateFlow(VerifyUiState())
    val uiState: StateFlow<VerifyUiState>
        get() = _uiState.asStateFlow()

    private var job: Job? = null

    init {
        viewModelScope.launch {
            val countries = countriesRepository.getCountriesResponse()
            countries.onSuccess {
                _uiState.update { current -> current.copy(isLoading = false, countries = it) }
            }
            countries.onFailure {
                _uiState.update { current -> current.copy(isLoading = false, error = it.message) }
            }
        }
    }

    fun verify(
        number: String,
        countryCode: String
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            _uiState.update { current -> current.copy(isLoading = true) }
            val validate = validationRepository.validate(
                number = number,
                countryCode = countryCode
            )
            validate.onSuccess {
                _uiState.update { current-> current.copy(isLoading = false, verification = it) }
            }

            validate.onFailure {
                _uiState.update { current -> current.copy(isLoading = false, error = it.message) }
            }
        }
    }
}
