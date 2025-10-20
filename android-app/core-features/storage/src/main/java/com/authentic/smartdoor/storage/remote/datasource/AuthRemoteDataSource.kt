package com.authentic.smartdoor.storage.remote.datasource

import com.authentic.smartdoor.storage.remote.api.AuthApiService
import com.authentic.smartdoor.storage.remote.dto.AuthData
import com.authentic.smartdoor.storage.remote.dto.GoogleAuthRequest
import retrofit2.HttpException
import javax.inject.Inject

interface AuthRemoteDataSource {
    suspend fun authenticateWithGoogle(
        idToken: String,
        email: String?,
        name: String?,
        picture: String?
    ): AuthData

    suspend fun logout(authHeader: String)
}

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRemoteDataSource {

    override suspend fun authenticateWithGoogle(
        idToken: String,
        email: String?,
        name: String?,
        picture: String?
    ): AuthData {
        val response = authApiService.authenticateWithGoogle(
            GoogleAuthRequest(
                id_token = idToken,
                email = email,
                name = name,
                picture = picture
            )
        )
        if (!response.isSuccessful) throw HttpException(response)
        val body = response.body() ?: throw IllegalStateException("Empty response body")
        if (body.data == null || body.success.not()) {
            throw IllegalStateException(body.message ?: "Authentication failed")
        }
        return body.data
    }

    override suspend fun logout(authHeader: String) {
        val response = authApiService.logout(authHeader)
        if (!response.isSuccessful) throw HttpException(response)
    }
}


