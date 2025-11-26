package com.authentic.smartdoor.authentication.presentation.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authentic.smartdoor.authentication.domain.usecase.GetCurrentUserUseCase
import com.authentic.smartdoor.authentication.domain.usecase.HandleGoogleSignInUseCase
import com.authentic.smartdoor.authentication.domain.usecase.IsLoggedInUseCase
import com.authentic.smartdoor.authentication.domain.usecase.SignOutUseCase
import com.authentic.smartdoor.authentication.utils.GoogleSignInHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val handleGoogleSignInUseCase: HandleGoogleSignInUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val googleSignInHelper: GoogleSignInHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        checkAuthStatus()
    }

    fun getGoogleSignInIntent(): Intent {
        android.util.Log.d("AuthViewModel", "=== getGoogleSignInIntent called ===")
        return try {
            android.util.Log.d("AuthViewModel", "Creating Google Sign-In intent...")
            val client = googleSignInHelper.getGoogleSignInClient()
            val intent = client.signInIntent
            android.util.Log.d("AuthViewModel", "Google Sign-In intent created successfully")
            intent
        } catch (e: Exception) {
            android.util.Log.e("AuthViewModel", "Failed to create Google Sign-In intent: ${e.message}", e)
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to initialize Google Sign-In: ${e.message}"
            )
            Intent()
        }
    }

    fun handleGoogleSignInResult(data: Intent?) {
        android.util.Log.d("AuthViewModel", "=== handleGoogleSignInResult called ===")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            if (data == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Google Sign-In was cancelled"
                )
                return@launch
            }

            try {
                android.util.Log.d("AuthViewModel", "Processing Google Sign-In result...")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                android.util.Log.d("AuthViewModel", "Google Sign-In task created, getting result...")
                val account = task.getResult(ApiException::class.java)
                android.util.Log.d("AuthViewModel", "Google Sign-In account: ${account?.email}")

                if (account == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to get Google account information"
                    )
                    return@launch
                }

                handleGoogleSignInUseCase(account).fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = user,
                            isSignedIn = true
                        )
                        _navigationEvent.emit(AuthNavigationEvent.NavigateToHome)
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Authentication failed"
                        )
                    }
                )
            } catch (e: ApiException) {
                android.util.Log.e("AuthViewModel", "Google Sign-In ApiException: Status ${e.statusCode}, Message: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Google Sign-In failed: Error ${e.statusCode} - ${e.message}"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unexpected error: ${e.message}"
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase().fold(
                onSuccess = {
                    _uiState.value = AuthUiState()
                    _navigationEvent.emit(AuthNavigationEvent.NavigateToLogin)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = exception.message ?: "Sign out failed"
                    )
                }
            )
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            if (isLoggedInUseCase()) {
                getCurrentUserUseCase().fold(
                    onSuccess = { user ->
                        if (user != null) {
                            _uiState.value = _uiState.value.copy(
                                user = user,
                                isSignedIn = true
                            )
                            _navigationEvent.emit(AuthNavigationEvent.NavigateToHome)
                        }
                    },
                    onFailure = {
                        // Handle error, maybe token expired
                    }
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}