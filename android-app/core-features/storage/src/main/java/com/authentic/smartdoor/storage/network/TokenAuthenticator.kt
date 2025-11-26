package com.authentic.smartdoor.storage.network

import com.authentic.smartdoor.storage.preferences.PreferencesManager
import com.authentic.smartdoor.storage.remote.api.AuthApiService
import com.authentic.smartdoor.storage.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val preferences: PreferencesManager,
    private val authApi: AuthApiService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val attempted = response.request.header("X-Refresh-Attempt") == "true"
        if (attempted) return null
        val refreshToken = preferences.getRefreshToken() ?: return null
        val newAccess: String? = runBlocking {
            val res = authApi.refreshToken(RefreshTokenRequest(refreshToken))
            if (res.isSuccessful) res.body()?.data?.tokens?.access_token else null
        }
        if (newAccess.isNullOrEmpty()) {
            preferences.clearAuthData()
            preferences.setLoggedIn(false)
            return null
        }
        preferences.saveAuthToken(newAccess)
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .header("X-Refresh-Attempt", "true")
            .build()
    }
}