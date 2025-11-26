package com.authentic.smartdoor.authentication.presentation.auth

import com.authentic.smartdoor.authentication.domain.model.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false
)