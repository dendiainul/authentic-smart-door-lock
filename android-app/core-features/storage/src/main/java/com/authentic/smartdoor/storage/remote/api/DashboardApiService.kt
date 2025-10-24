package com.authentic.smartdoor.storage.remote.api

import com.authentic.smartdoor.storage.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface DashboardApiService {
    @GET("door/status")
    suspend fun getDoorStatus(
        @Header("Authorization") token: String
    ): Response<DoorListResponse>

    @POST("door/control")
    suspend fun controlDoor(
        @Header("Authorization") token: String,
        @Body request: DoorControlRequest
    ): Response<DoorControlResponse>

    @GET("history/access")
    suspend fun getAccessHistory(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("door_id") doorId: Int? = null,
        @Query("user_id") userId: Int? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("success") success: Boolean? = null
    ): Response<AccessLogResponse>

    @GET("notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("read") read: Boolean? = null,
        @Query("type") type: String? = null,
        @Query("user_id") userId: Int? = null
    ): Response<NotificationResponse>

    @POST("notifications/mark-read")
    suspend fun markNotificationsAsRead(
        @Header("Authorization") token: String,
        @Body request: MarkReadRequest
    ): Response<Map<String, Any>>

    @GET("users")
    suspend fun getUsers(
        @Header("Authorization") token: String
    ): Response<UserListResponse>

    @GET("users/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<com.authentic.smartdoor.storage.remote.dto.UserProfileResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    // Camera endpoints
    @GET("camera/stream")
    suspend fun getCameraStream(
        @Header("Authorization") token: String,
        @Query("door_id") doorId: Int
    ): Response<CameraStreamResponse>

    @POST("camera/capture")
    suspend fun capturePhoto(
        @Header("Authorization") token: String,
        @Body request: CameraCaptureRequest
    ): Response<CameraCaptureCreateResponse>

    @GET("camera/capture/{id}")
    suspend fun getCameraCaptureById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<CameraCaptureDetailResponse>

    // History photos endpoint
    @GET("history/photos")
    suspend fun getPhotoHistory(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("door_id") doorId: Int? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("event_type") eventType: String? = null
    ): Response<CameraCaptureListResponse>

    // Analytics endpoints
    @GET("analytics/dashboard")
    suspend fun getAnalyticsDashboard(
        @Header("Authorization") token: String,
        @Query("door_id") doorId: Int? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): Response<AnalyticsResponse>

    // Optional future endpoints can be added here as backend evolves
}

data class UserListResponse(
    val success: Boolean,
    val data: List<com.authentic.smartdoor.storage.remote.dto.UserDto>?,
    val message: String?
)

data class UserProfileResponse(
    val success: Boolean,
    val data: com.authentic.smartdoor.storage.remote.dto.UserDto?,
    val message: String?
)


