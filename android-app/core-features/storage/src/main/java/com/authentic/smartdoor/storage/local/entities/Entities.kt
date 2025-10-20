package com.authentic.smartdoor.storage.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "google_id") val googleId: String,
    val email: String,
    val name: String,
    val role: String,
    @ColumnInfo(name = "face_data") val faceData: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)

@Entity(tableName = "access_logs")
data class AccessLogEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    val action: String,
    val timestamp: Long,
    val success: Boolean,
    val method: String,
    @ColumnInfo(name = "ip_address") val ipAddress: String
)

@Entity(tableName = "door_status")
data class DoorStatusEntity(
    @PrimaryKey val id: String,
    val name: String,
    val location: String,
    val locked: Boolean,
    @ColumnInfo(name = "battery_level") val batteryLevel: Int,
    @ColumnInfo(name = "last_update") val lastUpdate: Long,
    @ColumnInfo(name = "wifi_strength") val wifiStrength: String?,
    @ColumnInfo(name = "camera_active") val cameraActive: Boolean
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    val type: String,
    val title: String,
    val message: String,
    val read: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Long
)

@Entity(tableName = "camera_records")
data class CameraRecordEntity(
    @PrimaryKey val id: String,
    val filename: String,
    val timestamp: Long,
    @ColumnInfo(name = "event_type") val eventType: String,
    @ColumnInfo(name = "file_size") val fileSize: Long
)

@Entity(tableName = "system_settings")
data class SystemSettingEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "key")
    val settingKey: String,
    val value: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "updated_by")
    val updatedBy: String
)



