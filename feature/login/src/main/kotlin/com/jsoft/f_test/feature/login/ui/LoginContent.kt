package com.jsoft.f_test.feature.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsoft.f_test.core.ui.theme.F_testTheme
import com.jsoft.f_test.feature.login.LoginUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginUiState,
    onDateFieldClick: () -> Unit,
    onDatePickerDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    onPassportChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Xush kelibsiz",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Kirish uchun ma'lumotlaringizni kiriting",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.dateOfBirth,
                onValueChange = {},
                label = { Text("Tug'ilgan sana") },
                placeholder = { Text("DD/MM/YYYY") },
                singleLine = true,
                readOnly = true,
                enabled = !state.isLoading,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Sanani tanlash",
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            if (!state.isLoading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(onClick = onDateFieldClick)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.passport,
            onValueChange = onPassportChange,
            label = { Text("Pasport ma'lumoti") },
            placeholder = { Text("AA1234567") },
            singleLine = true,
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (state.passport.length < 2) {
                    KeyboardType.Text
                } else {
                    KeyboardType.NumberPassword
                },
                capitalization = KeyboardCapitalization.Characters,
            ),
            visualTransformation = if (state.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (state.isPasswordVisible) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        },
                        contentDescription = null,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.errorMessage != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text("Kirish")
            }
        }
    }

    if (state.isDatePickerVisible) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.selectedDobMillis,
        )
        DatePickerDialog(
            onDismissRequest = onDatePickerDismiss,
            confirmButton = {
                TextButton(
                    onClick = { onDateSelected(datePickerState.selectedDateMillis) },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDatePickerDismiss) {
                    Text("Bekor qilish")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentEmptyPreview() {
    F_testTheme {
        LoginContent(
            state = LoginUiState(),
            onDateFieldClick = {},
            onDatePickerDismiss = {},
            onDateSelected = {},
            onPassportChange = {},
            onPasswordVisibilityToggle = {},
            onLoginClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentFilledPreview() {
    F_testTheme {
        LoginContent(
            state = LoginUiState(
                dateOfBirth = "01/01/2000",
                passport = "AA1234567",
            ),
            onDateFieldClick = {},
            onDatePickerDismiss = {},
            onDateSelected = {},
            onPassportChange = {},
            onPasswordVisibilityToggle = {},
            onLoginClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentLoadingPreview() {
    F_testTheme {
        LoginContent(
            state = LoginUiState(
                dateOfBirth = "01/01/2000",
                passport = "AA1234567",
                isLoading = true,
            ),
            onDateFieldClick = {},
            onDatePickerDismiss = {},
            onDateSelected = {},
            onPassportChange = {},
            onPasswordVisibilityToggle = {},
            onLoginClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentErrorPreview() {
    F_testTheme {
        LoginContent(
            state = LoginUiState(
                dateOfBirth = "01/01/1999",
                passport = "XX9999999",
                errorMessage = "Login yoki parol noto'g'ri",
            ),
            onDateFieldClick = {},
            onDatePickerDismiss = {},
            onDateSelected = {},
            onPassportChange = {},
            onPasswordVisibilityToggle = {},
            onLoginClick = {},
        )
    }
}