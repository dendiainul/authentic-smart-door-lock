package com.authentic.smartdoor.storage.network

import com.authentic.smartdoor.storage.preferences.PreferencesManager
import com.authentic.smartdoor.storage.remote.api.AuthApiService
import com.authentic.smartdoor.storage.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenRefreshInterceptor(
    private val preferences: PreferencesManager,
    private val authApi: AuthApiService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code != 401) return response
        val path = request.url.encodedPath
        val attempted = request.header("X-Refresh-Attempt") == "true"
        if (path.endsWith("auth/refresh") || attempted) return response
        val refreshToken = preferences.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            preferences.clearAuthData()
            preferences.setLoggedIn(false)
            return response
        }
        val newAccess = runBlocking {
            val res = authApi.refreshToken(RefreshTokenRequest(refreshToken))
            if (res.isSuccessful) {
                res.body()?.data?.tokens?.access_token
            } else null
        }
        if (newAccess.isNullOrEmpty()) {
            preferences.clearAuthData()
            preferences.setLoggedIn(false)
            return response
        }
        response.close()
        preferences.saveAuthToken(newAccess)
        val newReq = request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .header("X-Refresh-Attempt", "true")
            .build()
        return chain.proceed(newReq)
    }
}