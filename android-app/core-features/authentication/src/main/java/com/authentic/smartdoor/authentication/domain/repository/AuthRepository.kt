package com.authentic.smartdoor.authentication.domain.repository

import com.authentic.smartdoor.authentication.domain.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AuthRepository {
    suspend fun signInWithGoogle(): Result<User>
    suspend fun handleGoogleSignInResult(account: GoogleSignInAccount): Result<User>
    suspend fun getCurrentUser(): Result<User?>
    suspend fun signOut(): Result<Unit>
    fun isLoggedIn(): Boolean
}