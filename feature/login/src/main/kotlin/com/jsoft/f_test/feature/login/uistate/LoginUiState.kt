package com.jsoft.f_test.feature.login

data class LoginUiState(
    val dateOfBirth: String = "",
    val selectedDobMillis: Long? = null,
    val isDatePickerVisible: Boolean = false,
    val passport: String = "",
    val isPasswordVisible: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
)