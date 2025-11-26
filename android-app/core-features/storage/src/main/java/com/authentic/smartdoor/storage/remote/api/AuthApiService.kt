package com.authentic.smartdoor.storage.remote.api

import com.authentic.smartdoor.storage.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/google")
    suspend fun authenticateWithGoogle(
        @Body request: GoogleAuthRequest
    ): Response<GoogleAuthResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>
}


