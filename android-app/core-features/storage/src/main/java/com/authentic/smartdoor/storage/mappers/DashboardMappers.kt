package com.authentic.smartdoor.storage.mappers

import com.authentic.smartdoor.storage.local.entities.AccessLogEntity
import com.authentic.smartdoor.storage.local.entities.DoorStatusEntity
import com.authentic.smartdoor.storage.local.entities.NotificationEntity
import com.authentic.smartdoor.storage.remote.dto.AccessLogDto
import com.authentic.smartdoor.storage.remote.dto.DoorDto
import com.authentic.smartdoor.storage.remote.dto.NotificationDto
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun DoorDto.toEntity(): DoorStatusEntity {
    val lastUpdateMillis = runCatching {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(last_update)?.time ?: System.currentTimeMillis()
    }.getOrElse {
        System.currentTimeMillis()
    }

    return DoorStatusEntity(
        id = id.toString(),
        name = name,
        location = location,
        locked = locked,
        batteryLevel = battery_level,
        lastUpdate = lastUpdateMillis,
        wifiStrength = wifi_strength,
        cameraActive = camera_active
    )
}

fun NotificationDto.toEntity(): NotificationEntity {
    val createdAtMillis = runCatching {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(created_at)?.time ?: System.currentTimeMillis()
    }.getOrElse {
        System.currentTimeMillis()
    }

    return NotificationEntity(
        id = id.toString(),
        userId = user_id?.toString() ?: "",
        type = type,
        title = type.replace("_", " ").split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } },
        message = message,
        read = read,
        createdAt = createdAtMillis
    )
}

fun AccessLogDto.toEntity(): AccessLogEntity {
    val timestampMillis = runCatching {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(timestamp)?.time ?: System.currentTimeMillis()
    }.getOrElse {
        System.currentTimeMillis()
    }

    return AccessLogEntity(
        id = id.toString(),
        userId = user_id.toString(),
        action = action,
        timestamp = timestampMillis,
        success = success,
        method = method,
        ipAddress = ip_address
    )
}

// Analytics Mappers - Generic types to avoid circular dependency
fun com.authentic.smartdoor.storage.remote.dto.AnalyticsMetricDto.toGenericMetric(): Pair<Int, String> {
    return Pair(value, change)
}

fun com.authentic.smartdoor.storage.remote.dto.ChartDataDto.toGenericChartData(): Pair<Int, Int> {
    return Pair(hour, count)
}

fun com.authentic.smartdoor.storage.remote.dto.ActiveHourDto.toGenericActiveHour(): Triple<String, Int, Double> {
    return Triple(timeRange, count, progress)
}

fun com.authentic.smartdoor.storage.remote.dto.AvailableDoorDto.toGenericAvailableDoor(): Triple<Int, String, String> {
    return Triple(id, name, location)
}

fun com.authentic.smartdoor.storage.remote.dto.AccessLogDto.toGenericAccessLog(): Nonuple<String, String, String, String, String, Boolean, String, String, String?> {
    return Nonuple(
        id.toString(),
        user_id.toString(),
        door_id.toString(),
        action,
        timestamp,
        success,
        method,
        ip_address,
        camera_capture_id?.toString()
    )
}

// Helper data class for 9-tuple
data class Nonuple<A, B, C, D, E, F, G, H, I>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G,
    val eighth: H,
    val ninth: I
)




