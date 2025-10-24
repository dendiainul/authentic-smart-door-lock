package com.authentic.smartdoor.dashboard.domain.repository

import com.authentic.smartdoor.dashboard.domain.model.*

interface DashboardRepository {
    suspend fun getDashboardData(): Result<DashboardData>
    suspend fun refreshData(): Result<DashboardData>
    suspend fun controlDoor(action: String): Result<Boolean>
    suspend fun controlDoorById(action: String, doorId: String): Result<Boolean>
    suspend fun markNotificationsAsRead(ids: List<String>): Result<Unit>
    suspend fun getUnreadNotificationCount(): Result<Int>
    suspend fun getAccessHistory(): Result<List<AccessLog>>
    suspend fun getUserProfile(): Result<com.authentic.smartdoor.dashboard.domain.model.User?>
    suspend fun logout(): Result<Unit>
    
    // Notification specific methods
    suspend fun getNotifications(): Result<List<Notification>>
    suspend fun refreshNotifications(): Result<List<Notification>>
    
    // Analytics methods
    suspend fun getAnalyticsData(doorId: Int? = null, startDate: String? = null, endDate: String? = null): Result<AnalyticsData>
}


