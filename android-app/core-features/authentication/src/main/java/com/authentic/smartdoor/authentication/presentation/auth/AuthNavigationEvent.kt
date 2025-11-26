package com.authentic.smartdoor.authentication.presentation.auth

sealed class AuthNavigationEvent {
    object NavigateToHome : AuthNavigationEvent()
    object NavigateToLogin : AuthNavigationEvent()
}