package com.authentic.smartdoor.dashboard.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.util.*

fun Notification.toNotificationModel(): NotificationModel {
    return NotificationModel(
        id = id,
        type = type,
        title = type.replace("_", " ").split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } },
        message = message,
        read = read,
        createdAt = createdAt
    )
}

data class NotificationModel(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val read: Boolean,
    val createdAt: String
) {
    val formattedTime: String
        get() {
            return try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                formatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val date = formatter.parse(createdAt)
                val now = System.currentTimeMillis()
                val diff = now - (date?.time ?: now)
                
                when {
                    diff < 60_000 -> "Baru saja"
                    diff < 3600_000 -> "${diff / 60_000} menit yang lalu"
                    diff < 86400_000 -> "${diff / 3600_000} jam yang lalu"
                    diff < 604800_000 -> "${diff / 86400_000} hari yang lalu"
                    else -> {
                        val displayFormatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                        displayFormatter.format(Date(date?.time ?: now))
                    }
                }
            } catch (e: Exception) {
                "Waktu tidak diketahui"
            }
        }
    
    val icon: ImageVector
        get() = when (type.lowercase()) {
            "door_open" -> Icons.Default.Check
            "door_close" -> Icons.Default.Lock
            "access_denied" -> Icons.Default.Warning
            "system_maintenance" -> Icons.Default.Settings
            "security_alert" -> Icons.Default.Security
            "battery_low" -> Icons.Default.BatteryAlert
            else -> Icons.Default.Notifications
        }
    
    val iconTint: Color
        get() = when (type.lowercase()) {
            "door_open" -> Color(0xFF4CAF50)
            "door_close" -> Color(0xFF2196F3)
            "access_denied" -> Color(0xFFFFC756)
            "system_maintenance" -> Color(0xFF9C27B0)
            "security_alert" -> Color(0xFFF44336)
            "battery_low" -> Color(0xFFFF9800)
            else -> Color.Black
        }
    
    val iconBackground: Color
        get() = when (type.lowercase()) {
            "door_open" -> Color(0xFFE8F5E8)
            "door_close" -> Color(0xFFE3F2FD)
            "access_denied" -> Color(0xFFFFF8E1)
            "system_maintenance" -> Color(0xFFF3E5F5)
            "security_alert" -> Color(0xFFFFEBEE)
            "battery_low" -> Color(0xFFFFF3E0)
            else -> Color(0xFFF0E5FF)
        }
}
