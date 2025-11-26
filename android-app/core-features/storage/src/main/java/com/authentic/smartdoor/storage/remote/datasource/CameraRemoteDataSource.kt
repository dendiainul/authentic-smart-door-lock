package com.authentic.smartdoor.storage.remote.datasource

import com.authentic.smartdoor.storage.remote.api.CameraApiService
import com.authentic.smartdoor.storage.preferences.PreferencesManager
import com.authentic.smartdoor.storage.remote.dto.CameraStreamResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraRemoteDataSource @Inject constructor(
    private val apiService: CameraApiService,
    private val preferencesManager: PreferencesManager
) {

    private fun bearer(): String? = preferencesManager.getAuthToken()?.let { "Bearer $it" }

    suspend fun getCameraStream(doorId: Int): CameraStreamResponse {
        val token = bearer() ?: ""
        val response = apiService.getCameraStream(token = token, doorId = doorId)
        return if (response.isSuccessful) {
            response.body() ?: CameraStreamResponse(success = false, data = null, message = "Empty body")
        } else {
            CameraStreamResponse(success = false, data = null, message = response.errorBody()?.string())
        }
    }
}