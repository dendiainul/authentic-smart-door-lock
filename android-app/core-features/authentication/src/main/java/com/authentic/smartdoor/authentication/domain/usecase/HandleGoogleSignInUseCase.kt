package com.authentic.smartdoor.authentication.domain.usecase

import com.authentic.smartdoor.authentication.domain.model.User
import com.authentic.smartdoor.authentication.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class HandleGoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(account: GoogleSignInAccount): Result<User> {
        return authRepository.handleGoogleSignInResult(account)
    }
}