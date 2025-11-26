package com.authentic.smartdoor.storage.remote.api

import com.authentic.smartdoor.storage.remote.dto.CameraStreamResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CameraApiService {
    @GET("camera/stream")
    suspend fun getCameraStream(
        @Header("Authorization") token: String,
        @Query("door_id") doorId: Int
    ): Response<CameraStreamResponse>
}