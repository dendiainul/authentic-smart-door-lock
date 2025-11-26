package com.authentic.smartdoor.storage.remote.dto

data class DoorDto(
    val id: Int,
    val name: String,
    val location: String,
    val locked: Boolean,
    val battery_level: Int,
    val last_update: String,
    val wifi_strength: String,
    val camera_active: Boolean,
    val access_granted_at: String? = null
)

data class DoorListResponse(
    val success: Boolean,
    val data: List<DoorDto>?,
    val message: String?
)

data class NotificationDto(
    val id: Int,
    val user_id: Int?,
    val type: String,
    val message: String,
    val read: Boolean,
    val created_at: String
)

data class NotificationResponse(
    val success: Boolean,
    val data: List<NotificationDto>?,
    val message: String?
)

// Removed NotificationCountResponse; backend provides pagination instead

data class AccessLogDto(
    val id: Int,
    val user_id: Int,
    val door_id: Int,
    val action: String,
    val timestamp: String,
    val success: Boolean,
    val method: String?,
    val ip_address: String,
    val camera_capture_id: Int?,
    val user: UserDto? = null,
    val door: DoorDto? = null,
    val camera_capture: CameraCaptureDto? = null
)

data class AccessLogResponse(
    val success: Boolean,
    val data: List<AccessLogDto>?,
    val message: String?
)

// Minimal camera capture summary used inside access logs
data class CameraCaptureDto(
    val id: Int,
    val filename: String,
    val event_type: String,
    val timestamp: String
)

data class MarkReadRequest(
    val notification_ids: List<Int>
)

data class DoorControlRequest(
    val action: String,
    val door_id: Int? = null
)

data class DoorControlResponse(
    val success: Boolean,
    val message: String?,
    val data: DoorControlData?
)


data class DoorControlData(
    val action: String,
    val success: Boolean,
    val timestamp: String,
    val door_id: Int?
)

data class UserProfileResponse(
    val success: Boolean,
    val data: com.authentic.smartdoor.storage.remote.dto.UserDto?,
    val message: String?
)

// Analytics DTOs
data class AnalyticsMetricDto(
    val value: Int,
    val change: String,
    val changeType: String
)

data class ChartDataDto(
    val hour: Int,
    val count: Int
)

data class ActiveHourDto(
    val timeRange: String,
    val count: Int,
    val progress: Double
)

data class AvailableDoorDto(
    val id: Int,
    val name: String,
    val location: String
)

data class AnalyticsDataDto(
    val metrics: AnalyticsMetricsDto,
    val chartData: List<ChartDataDto>,
    val activeHours: List<ActiveHourDto>,
    val availableDoors: List<AvailableDoorDto>,
    val accessLogs: List<AccessLogDto>? = null
)

data class AnalyticsMetricsDto(
    val totalAccess: AnalyticsMetricDto,
    val deniedAccess: AnalyticsMetricDto,
    val lockedDoors: AnalyticsMetricDto,
    val openedDoors: AnalyticsMetricDto
)

data class AnalyticsResponse(
    val success: Boolean,
    val data: AnalyticsDataDto?,
    val message: String?
)


