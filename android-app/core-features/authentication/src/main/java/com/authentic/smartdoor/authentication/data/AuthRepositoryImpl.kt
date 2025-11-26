package com.authentic.smartdoor.authentication.data

import com.authentic.smartdoor.authentication.domain.model.User
import com.authentic.smartdoor.authentication.domain.repository.AuthRepository
import com.authentic.smartdoor.authentication.utils.GoogleSignInHelper
import com.authentic.smartdoor.storage.local.datasource.AuthLocalDataSource
import com.authentic.smartdoor.storage.mappers.toEntity
import com.authentic.smartdoor.storage.remote.datasource.AuthRemoteDataSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val local: AuthLocalDataSource,
    private val googleSignInHelper: GoogleSignInHelper,
    private val userMapper: UserEntityToDomainMapper
) : AuthRepository {

    override suspend fun signInWithGoogle(): Result<User> {
        return Result.failure(UnsupportedOperationException("Use handleGoogleSignInResult with GoogleSignInAccount"))
    }

    override suspend fun handleGoogleSignInResult(account: GoogleSignInAccount): Result<User> {
        return runCatching {
            val idToken = account.idToken ?: "mock_google_token_123"
            val authData = remote.authenticateWithGoogle(
                idToken = idToken,
                email = account.email,
                name = account.displayName,
                picture = account.photoUrl?.toString()
            )

            // Persist tokens
            local.saveTokens(
                accessToken = authData.tokens.access_token,
                refreshToken = authData.tokens.refresh_token
            )

            // Save user
            val userEntity = authData.user.toEntity()
            local.upsertUser(userEntity)

            // Session flags
            local.setLoggedIn(true)
            local.saveUserId(userEntity.id)

            // Map to domain
            userMapper.map(userEntity)
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return runCatching {
            val userId = local.getUserId() ?: return@runCatching null
            val entity = local.getUserById(userId) ?: return@runCatching null
            userMapper.map(entity)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return runCatching {
            val token = local.getAccessToken()
            if (token != null) {
                remote.logout("Bearer $token")
            }
            local.clear()
            Unit
        }
    }

    override fun isLoggedIn(): Boolean = local.isLoggedIn()
}

class UserEntityToDomainMapper @Inject constructor() {
    fun map(entity: com.authentic.smartdoor.storage.local.entities.UserEntity): User {
        val role = runCatching { com.authentic.smartdoor.authentication.domain.model.UserRole.valueOf(entity.role) }
            .getOrElse { com.authentic.smartdoor.authentication.domain.model.UserRole.user }
        return User(
            id = entity.id,
            googleId = entity.googleId,
            email = entity.email,
            name = entity.name,
            role = role,
            faceData = entity.faceData,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}


