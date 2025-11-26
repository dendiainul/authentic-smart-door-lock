package com.authentic.smartdoor.storage.local.datasource

import com.authentic.smartdoor.storage.local.dao.UserDao
import com.authentic.smartdoor.storage.local.entities.UserEntity
import com.authentic.smartdoor.storage.preferences.PreferencesManager
import javax.inject.Inject

interface AuthLocalDataSource {
    suspend fun upsertUser(user: UserEntity)
    suspend fun getUserById(userId: String): UserEntity?
    fun saveTokens(accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun setLoggedIn(isLoggedIn: Boolean)
    fun isLoggedIn(): Boolean
    fun saveUserId(userId: String)
    fun getUserId(): String?
    fun clear()
}

class AuthLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val preferencesManager: PreferencesManager
) : AuthLocalDataSource {
    override suspend fun upsertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun getUserById(userId: String): UserEntity? {
        return userDao.getUserById(userId)
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        preferencesManager.saveAuthToken(accessToken)
        preferencesManager.saveRefreshToken(refreshToken)
    }

    override fun getAccessToken(): String? = preferencesManager.getAuthToken()

    override fun getRefreshToken(): String? = preferencesManager.getRefreshToken()

    override fun setLoggedIn(isLoggedIn: Boolean) = preferencesManager.setLoggedIn(isLoggedIn)

    override fun isLoggedIn(): Boolean = preferencesManager.isLoggedIn()

    override fun saveUserId(userId: String) = preferencesManager.saveUserId(userId)

    override fun getUserId(): String? = preferencesManager.getUserId()

    override fun clear() = preferencesManager.clearAuthData()
}


