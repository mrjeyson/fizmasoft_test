package com.jsoft.f_test.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsoft.f_test.core.common.result.AppResult
import com.jsoft.f_test.domain.auth.model.Credentials
import com.jsoft.f_test.domain.auth.usecase.LoginUseCase
import com.jsoft.f_test.feature.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val displayDateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun onDateFieldClick() {
        _uiState.update { it.copy(isDatePickerVisible = true) }
    }

    fun onDatePickerDismiss() {
        _uiState.update { it.copy(isDatePickerVisible = false) }
    }

    fun onDateSelected(millis: Long?) {
        val formatted = millis?.let { displayDateFormatter.format(Date(it)) } ?: ""
        _uiState.update {
            it.copy(
                selectedDobMillis = millis,
                dateOfBirth = formatted,
                isDatePickerVisible = false,
                errorMessage = null,
            )
        }
    }

    fun onPassportChange(value: String) {
        val filtered = buildString {
            value.forEachIndexed { index, char ->
                when {
                    index < 2 && char.isLatinLetter() -> append(char.uppercaseChar())
                    index in 2..8 && char.isDigit() -> append(char)
                }
            }
        }.take(9)

        _uiState.update { it.copy(passport = filtered, errorMessage = null) }
    }

    private fun Char.isLatinLetter(): Boolean = this in 'a'..'z' || this in 'A'..'Z'

    fun onPasswordVisibilityToggle() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val current = _uiState.value

        if (current.dateOfBirth.isBlank() || current.passport.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Barcha maydonlarni to'ldiring") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val credentials = Credentials(
                dateOfBirth = current.dateOfBirth,
                passport = current.passport,
            )

            when (val result = loginUseCase(credentials)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.message ?: "Xatolik yuz berdi",
                        )
                    }
                }
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}