package com.authentic.smartdoor.dashboard.data.repository

import com.authentic.smartdoor.dashboard.domain.model.AccessLog
import com.authentic.smartdoor.dashboard.domain.model.DashboardData
import com.authentic.smartdoor.dashboard.domain.model.SystemStatus
import com.authentic.smartdoor.dashboard.domain.repository.DashboardRepository
import com.authentic.smartdoor.storage.mappers.toEntity
import com.authentic.smartdoor.storage.mappers.toGenericMetric
import com.authentic.smartdoor.storage.mappers.toGenericChartData
import com.authentic.smartdoor.storage.mappers.toGenericActiveHour
import com.authentic.smartdoor.storage.mappers.toGenericAvailableDoor
import com.authentic.smartdoor.storage.mappers.toGenericAccessLog
import com.authentic.smartdoor.storage.remote.datasource.DashboardRemoteDataSource
import com.authentic.smartdoor.dashboard.data.mappers.toDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val remote: DashboardRemoteDataSource,
    private val local: com.authentic.smartdoor.storage.local.datasource.DashboardLocalDataSource,
    private val doorMapper: DoorEntityToDomainMapper,
    private val notificationMapper: NotificationEntityToDomainMapper,
    private val accessLogMapper: AccessLogEntityToDomainMapper
) : DashboardRepository {

    override suspend fun getDashboardData(): Result<DashboardData> {
        return runCatching {
            val doorsRes = remote.getDoorStatus()
            val notifRes = remote.getNotifications(limit = 20)
            val logsRes = remote.getAccessHistory(limit = 10)
            val userRes = remote.getUserProfile()

            val doors = doorsRes.data.orEmpty().map { doorMapper.map(it.toEntity()) }
            val notifications = notifRes.data.orEmpty().map { notificationMapper.map(it.toEntity()) }
            val accessLogs = logsRes.data.orEmpty().map { it.toDomainModel() }
            val user = userRes.data?.let { userDto ->
                com.authentic.smartdoor.dashboard.domain.model.User(
                    id = userDto.id.toString(),
                    name = userDto.name,
                    email = userDto.email,
                    avatar = userDto.avatar,
                    role = userDto.role,
                    faceRegistered = userDto.face_registered
                )
            }

            // Clear old data and save fresh data to local database
            local.clearAll()
            val doorEntities = doorsRes.data.orEmpty().map { it.toEntity() }
            local.saveDoorStatuses(doorEntities)

            // Save notifications to local database
            val notificationEntities = notifRes.data.orEmpty().map { it.toEntity() }
            local.saveNotifications(notificationEntities)

            val systemStatus = SystemStatus(
                doorsOnline = doors.count { !it.locked },
                camerasActive = doors.count { it.cameraActive },
                batteryOk = doors.all { it.batteryLevel >= 20 }
            )

            DashboardData(
                user = user,
                doors = doors,
                notifications = notifications,
                recentAccessLogs = accessLogs,
                systemStatus = systemStatus
            )
        }
    }

    override suspend fun refreshData(): Result<DashboardData> = getDashboardData()

    override suspend fun controlDoor(action: String): Result<Boolean> {
        return runCatching {
            val res = remote.controlDoor(action)
            res.success && (res.data?.success == true)
        }
    }

    override suspend fun controlDoorById(action: String, doorId: String): Result<Boolean> {
        return runCatching {
            val id = doorId.toIntOrNull()
            val res = remote.controlDoor(action, id)
            res.success && (res.data?.success == true)
        }
    }

    override suspend fun markNotificationsAsRead(ids: List<String>): Result<Unit> {
        return runCatching {
            val intIds = ids.mapNotNull { it.toIntOrNull() }
            remote.markNotificationsAsRead(intIds)
            Unit
        }
    }

    override suspend fun getUnreadNotificationCount(): Result<Int> {
        return runCatching {
            val notif = remote.getNotifications(limit = 100)
            notif.data.orEmpty().count { !it.read }
        }
    }

    override suspend fun getAccessHistory(): Result<List<AccessLog>> {
        return runCatching {
            val logs = remote.getAccessHistory(limit = 50)
            logs.data.orEmpty().map { it.toDomainModel() }
        }
    }

    override suspend fun getUserProfile(): Result<com.authentic.smartdoor.dashboard.domain.model.User?> {
        return runCatching {
            val userRes = remote.getUserProfile()
            userRes.data?.let { userDto ->
                com.authentic.smartdoor.dashboard.domain.model.User(
                    id = userDto.id.toString(),
                    name = userDto.name,
                    email = userDto.email,
                    avatar = userDto.avatar,
                    role = userDto.role,
                    faceRegistered = userDto.face_registered
                )
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            remote.logout()
            Unit
        }
    }

    override suspend fun getNotifications(): Result<List<com.authentic.smartdoor.dashboard.domain.model.Notification>> {
        return runCatching {
            val notifRes = remote.getNotifications(limit = 50)
            notifRes.data.orEmpty().map { notificationMapper.map(it.toEntity()) }
        }
    }

    override suspend fun refreshNotifications(): Result<List<com.authentic.smartdoor.dashboard.domain.model.Notification>> {
        return runCatching {
            val notifRes = remote.getNotifications(limit = 50)
            val notifications = notifRes.data.orEmpty().map { notificationMapper.map(it.toEntity()) }
            
            // Save to local database
            val notificationEntities = notifRes.data.orEmpty().map { it.toEntity() }
            local.saveNotifications(notificationEntities)
            
            notifications
        }
    }

    override suspend fun getAnalyticsData(doorId: Int?, startDate: String?, endDate: String?): Result<com.authentic.smartdoor.dashboard.domain.model.AnalyticsData> {
        return runCatching {
            val analyticsRes = remote.getAnalyticsDashboard(doorId, startDate, endDate)
            val data = analyticsRes.data ?: throw Exception("No analytics data received")
            
            // Convert DTOs to domain models using generic mappers
            val totalAccessMetric = data.metrics.totalAccess.toGenericMetric()
            val deniedAccessMetric = data.metrics.deniedAccess.toGenericMetric()
            val lockedDoorsMetric = data.metrics.lockedDoors.toGenericMetric()
            val openedDoorsMetric = data.metrics.openedDoors.toGenericMetric()
            
            val analyticsMetrics = com.authentic.smartdoor.dashboard.domain.model.AnalyticsMetrics(
                totalAccess = com.authentic.smartdoor.dashboard.domain.model.AnalyticsMetric(
                    value = totalAccessMetric.first,
                    change = totalAccessMetric.second,
                    changeType = data.metrics.totalAccess.changeType
                ),
                deniedAccess = com.authentic.smartdoor.dashboard.domain.model.AnalyticsMetric(
                    value = deniedAccessMetric.first,
                    change = deniedAccessMetric.second,
                    changeType = data.metrics.deniedAccess.changeType
                ),
                lockedDoors = com.authentic.smartdoor.dashboard.domain.model.AnalyticsMetric(
                    value = lockedDoorsMetric.first,
                    change = lockedDoorsMetric.second,
                    changeType = data.metrics.lockedDoors.changeType
                ),
                openedDoors = com.authentic.smartdoor.dashboard.domain.model.AnalyticsMetric(
                    value = openedDoorsMetric.first,
                    change = openedDoorsMetric.second,
                    changeType = data.metrics.openedDoors.changeType
                )
            )
            
            val chartData = data.chartData.map { chartDto ->
                val chartPair = chartDto.toGenericChartData()
                com.authentic.smartdoor.dashboard.domain.model.ChartData(
                    hour = chartPair.first,
                    count = chartPair.second
                )
            }
            
            val activeHours = data.activeHours.map { activeHourDto ->
                val activeHourTriple = activeHourDto.toGenericActiveHour()
                com.authentic.smartdoor.dashboard.domain.model.ActiveHour(
                    timeRange = activeHourTriple.first,
                    count = activeHourTriple.second,
                    progress = activeHourTriple.third
                )
            }
            
            val availableDoors = data.availableDoors.map { doorDto ->
                val doorTriple = doorDto.toGenericAvailableDoor()
                com.authentic.smartdoor.dashboard.domain.model.AvailableDoor(
                    id = doorTriple.first,
                    name = doorTriple.second,
                    location = doorTriple.third
                )
            }
            
            // Get access logs for chart data
            val accessLogs = data.accessLogs?.map { logDto ->
                val logNonuple = logDto.toGenericAccessLog()
                com.authentic.smartdoor.dashboard.domain.model.AccessLog(
                    id = logNonuple.first,
                    userId = logNonuple.second,
                    doorId = logNonuple.third,
                    action = logNonuple.fourth,
                    timestamp = logNonuple.fifth,
                    success = logNonuple.sixth,
                    method = logNonuple.seventh,
                    ipAddress = logNonuple.eighth,
                    cameraCaptureId = logNonuple.ninth
                )
            }
            
            com.authentic.smartdoor.dashboard.domain.model.AnalyticsData(
                metrics = analyticsMetrics,
                chartData = chartData,
                activeHours = activeHours,
                availableDoors = availableDoors,
                accessLogs = accessLogs
            )
        }
    }
}

class DoorEntityToDomainMapper @Inject constructor() {
    fun map(entity: com.authentic.smartdoor.storage.local.entities.DoorStatusEntity): com.authentic.smartdoor.dashboard.domain.model.Door {
        return com.authentic.smartdoor.dashboard.domain.model.Door(
            id = entity.id,
            name = entity.name,
            location = entity.location,
            locked = entity.locked,
            batteryLevel = entity.batteryLevel,
            lastUpdate = entity.lastUpdate?.let { 
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
                    .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
                    .format(java.util.Date(it))
            },
            wifiStrength = entity.wifiStrength,
            cameraActive = entity.cameraActive
        )
    }
}

class NotificationEntityToDomainMapper @Inject constructor() {
    fun map(entity: com.authentic.smartdoor.storage.local.entities.NotificationEntity): com.authentic.smartdoor.dashboard.domain.model.Notification {
        return com.authentic.smartdoor.dashboard.domain.model.Notification(
            id = entity.id,
            message = entity.message,
            type = entity.type,
            read = entity.read,
            createdAt = "" // Convert timestamp back to string if needed
        )
    }
}

class AccessLogEntityToDomainMapper @Inject constructor() {
    fun map(entity: com.authentic.smartdoor.storage.local.entities.AccessLogEntity): com.authentic.smartdoor.dashboard.domain.model.AccessLog {
        return com.authentic.smartdoor.dashboard.domain.model.AccessLog(
            id = entity.id,
            userId = entity.userId,
            doorId = "", // Entity doesn't store doorId, would need to be handled differently
            action = entity.action,
            timestamp = "", // Convert timestamp back to string if needed
            success = entity.success,
            method = entity.method,
            ipAddress = entity.ipAddress,
            cameraCaptureId = null // Entity doesn't store camera capture ID
        )
    }
}

