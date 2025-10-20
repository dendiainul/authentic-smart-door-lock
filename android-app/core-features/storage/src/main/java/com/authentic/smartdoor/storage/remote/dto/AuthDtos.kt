package com.authentic.smartdoor.storage.remote.dto

data class GoogleAuthRequest(
    val id_token: String,
    val email: String? = null,
    val name: String? = null,
    val picture: String? = null
)

data class GoogleAuthResponse(
    val success: Boolean,
    val message: String?,
    val data: AuthData?
)

data class AuthData(
    val user: UserDto,
    val tokens: TokenData
)

data class TokenData(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val expires_in: Int
)

data class UserDto(
    val id: Int,
    val google_id: String? = null,
    val name: String,
    val email: String,
    val role: String,
    val face_registered: Boolean,
    val created_at: String,
    val phone: String? = null,
    val avatar: String? = null
)

data class UserResponse(
    val success: Boolean,
    val data: UserDto?,
    val message: String?
)

data class RefreshTokenRequest(
    val refresh_token: String
)

data class RefreshTokenResponse(
    val success: Boolean,
    val data: RefreshTokenData?,
    val message: String?
)

data class RefreshTokenData(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)


