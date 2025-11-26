package com.authentic.smartdoor.storage.remote.datasource

import com.authentic.smartdoor.storage.preferences.PreferencesManager
import com.authentic.smartdoor.storage.remote.api.DashboardApiService
import com.authentic.smartdoor.storage.remote.api.UserListResponse
import com.authentic.smartdoor.storage.remote.dto.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRemoteDataSource @Inject constructor(
    private val api: DashboardApiService,
    private val preferences: PreferencesManager
) {
    private fun bearer(): String {
        val token = preferences.getAuthToken() ?: ""
        return if (token.startsWith("Bearer ")) token else "Bearer $token"
    }

    suspend fun getDoorStatus(): DoorListResponse {
        val response = api.getDoorStatus(bearer())
        if (response.isSuccessful) {
            return response.body() ?: DoorListResponse(false, emptyList(), "Empty body")
        }
        return DoorListResponse(false, emptyList(), response.errorBody()?.string())
    }

    suspend fun controlDoor(action: String, doorId: Int? = null): DoorControlResponse {
        val response = api.controlDoor(bearer(), DoorControlRequest(action = action, door_id = doorId))
        if (response.isSuccessful) {
            return response.body() ?: DoorControlResponse(false, "Empty body", null)
        }
        return DoorControlResponse(false, response.errorBody()?.string(), null)
    }

    suspend fun getAccessHistory(
        limit: Int? = null,
        offset: Int? = null,
        doorId: Int? = null,
        userId: Int? = null,
        startDate: String? = null,
        endDate: String? = null,
        success: Boolean? = null
    ): AccessLogResponse {
        val response = api.getAccessHistory(
            bearer(), limit, offset, doorId, userId, startDate, endDate, success
        )
        if (response.isSuccessful) {
            return response.body() ?: AccessLogResponse(false, emptyList(), "Empty body")
        }
        return AccessLogResponse(false, emptyList(), response.errorBody()?.string())
    }

    suspend fun getNotifications(
        limit: Int? = null,
        offset: Int? = null,
        read: Boolean? = null,
        type: String? = null,
        userId: Int? = null
    ): NotificationResponse {
        val response = api.getNotifications(bearer(), limit, offset, read, type, userId)
        if (response.isSuccessful) {
            return response.body() ?: NotificationResponse(false, emptyList(), "Empty body")
        }
        return NotificationResponse(false, emptyList(), response.errorBody()?.string())
    }

    suspend fun markNotificationsAsRead(ids: List<Int>): Boolean {
        val response = api.markNotificationsAsRead(bearer(), MarkReadRequest(notification_ids = ids))
        return response.isSuccessful
    }

    suspend fun getUsers(): UserListResponse {
        val response = api.getUsers(bearer())
        if (response.isSuccessful) {
            return response.body() ?: UserListResponse(false, emptyList(), "Empty body")
        }
        return UserListResponse(false, emptyList(), response.errorBody()?.string())
    }

    suspend fun getUserProfile(): com.authentic.smartdoor.storage.remote.dto.UserProfileResponse {
        val response = api.getUserProfile(bearer())
        if (response.isSuccessful) {
            return response.body() ?: com.authentic.smartdoor.storage.remote.dto.UserProfileResponse(false, null, "Empty body")
        }
        return com.authentic.smartdoor.storage.remote.dto.UserProfileResponse(false, null, response.errorBody()?.string())
    }

    suspend fun getCameraStream(doorId: Int): CameraStreamResponse {
        val response = api.getCameraStream(bearer(), doorId)
        if (response.isSuccessful) {
            return response.body() ?: CameraStreamResponse(false, null, "Empty body")
        }
        return CameraStreamResponse(false, null, response.errorBody()?.string())
    }

    suspend fun logout(): Boolean {
        val response = api.logout(bearer())
        return response.isSuccessful
    }

    suspend fun getAnalyticsDashboard(doorId: Int?, startDate: String?, endDate: String?): AnalyticsResponse {
        val response = api.getAnalyticsDashboard(bearer(), doorId, startDate, endDate)
        if (response.isSuccessful) {
            return response.body() ?: AnalyticsResponse(false, null, "Empty body")
        }
        return AnalyticsResponse(false, null, response.errorBody()?.string())
    }
}


