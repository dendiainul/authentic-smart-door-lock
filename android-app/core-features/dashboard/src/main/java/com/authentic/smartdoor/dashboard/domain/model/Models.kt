package com.authentic.smartdoor.dashboard.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String?,
    val avatar: String?,
    val role: String? = null,
    val faceRegistered: Boolean? = null
)

data class Door(
    val id: String,
    val name: String,
    val location: String,
    val locked: Boolean,
    val batteryLevel: Int,
    val lastUpdate: String?,
    val wifiStrength: String?,
    val cameraActive: Boolean
)

data class Notification(
    val id: String,
    val message: String,
    val type: String,
    val read: Boolean,
    val createdAt: String
)

data class AccessLog(
    val id: String,
    val userId: String,
    val doorId: String,
    val action: String,
    val timestamp: String,
    val success: Boolean,
    val method: String,
    val ipAddress: String,
    val cameraCaptureId: String?,
    val user: User? = null,
    val door: Door? = null,
    val cameraCapture: CameraCapture? = null
)

data class CameraCapture(
    val id: String,
    val filename: String,
    val eventType: String,
    val timestamp: String
)

data class SystemStatus(
    val doorsOnline: Int,
    val camerasActive: Int,
    val batteryOk: Boolean
)

data class DashboardData(
    val user: User?,
    val doors: List<Door>,
    val notifications: List<Notification>,
    val recentAccessLogs: List<AccessLog>,
    val systemStatus: SystemStatus?
)

// Analytics Models
data class AnalyticsMetric(
    val value: Int,
    val change: String,
    val changeType: String // "positive" or "negative"
)

data class ChartData(
    val hour: Int,
    val count: Int
)

data class ActiveHour(
    val timeRange: String,
    val count: Int,
    val progress: Double
)

data class AvailableDoor(
    val id: Int,
    val name: String,
    val location: String
)

data class AnalyticsData(
    val metrics: AnalyticsMetrics,
    val chartData: List<ChartData>,
    val activeHours: List<ActiveHour>,
    val availableDoors: List<AvailableDoor>,
    val accessLogs: List<AccessLog>? = null
)

data class AnalyticsMetrics(
    val totalAccess: AnalyticsMetric,
    val deniedAccess: AnalyticsMetric,
    val lockedDoors: AnalyticsMetric,
    val openedDoors: AnalyticsMetric
)


