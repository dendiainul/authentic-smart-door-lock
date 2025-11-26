package com.authentic.smartdoor.authentication.domain.usecase

import com.authentic.smartdoor.authentication.domain.model.User
import com.authentic.smartdoor.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return authRepository.signInWithGoogle()
    }
}