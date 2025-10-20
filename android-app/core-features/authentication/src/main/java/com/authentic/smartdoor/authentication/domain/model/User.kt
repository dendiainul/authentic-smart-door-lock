package com.authentic.smartdoor.authentication.domain.model

data class User(
    val id: String,
    val googleId: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val faceData: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)

enum class UserRole {
    admin, user
}
