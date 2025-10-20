package com.authentic.smartdoor.storage.mappers

import com.authentic.smartdoor.storage.local.entities.UserEntity
import com.authentic.smartdoor.storage.remote.dto.UserDto
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun UserDto.toEntity(): UserEntity {
    val createdAtMillis = runCatching {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(created_at)?.time ?: System.currentTimeMillis()
    }.getOrElse {
        System.currentTimeMillis()
    }

    return UserEntity(
        id = id.toString(),
        googleId = google_id ?: "",
        email = email,
        name = name,
        role = role,
        faceData = null,
        createdAt = createdAtMillis,
        updatedAt = System.currentTimeMillis()
    )
}


