package com.authentic.smartdoor.storage.local.datasource

import com.authentic.smartdoor.storage.local.dao.DoorStatusDao
import com.authentic.smartdoor.storage.local.dao.NotificationDao
import com.authentic.smartdoor.storage.local.entities.DoorStatusEntity
import com.authentic.smartdoor.storage.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DashboardLocalDataSource {
    suspend fun saveDoorStatuses(doors: List<DoorStatusEntity>)
    suspend fun getDoorStatuses(): List<DoorStatusEntity>
    suspend fun saveNotifications(notifications: List<NotificationEntity>)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)
    suspend fun getNotifications(): List<NotificationEntity>
    fun getNotificationsFlow(): Flow<List<NotificationEntity>>
    suspend fun markNotificationsAsRead(notificationIds: List<String>)
    suspend fun clearAll()
}

class DashboardLocalDataSourceImpl @Inject constructor(
    private val doorStatusDao: DoorStatusDao,
    private val notificationDao: NotificationDao
) : DashboardLocalDataSource {
    
    override suspend fun saveDoorStatuses(doors: List<DoorStatusEntity>) {
        doorStatusDao.insertAllStatuses(doors)
    }
    
    override suspend fun getDoorStatuses(): List<DoorStatusEntity> {
        return doorStatusDao.getAllStatuses()
    }
    
    override suspend fun saveNotifications(notifications: List<NotificationEntity>) {
        notifications.forEach { notificationDao.insertNotification(it) }
    }
    
    override suspend fun insertNotifications(notifications: List<NotificationEntity>) {
        notifications.forEach { notificationDao.insertNotification(it) }
    }
    
    override suspend fun getNotifications(): List<NotificationEntity> {
        return notificationDao.getNotificationsForUser("") // Get all notifications
    }
    
    override fun getNotificationsFlow(): Flow<List<NotificationEntity>> {
        return notificationDao.getNotificationsFlow()
    }
    
    override suspend fun markNotificationsAsRead(notificationIds: List<String>) {
        notificationDao.markNotificationsAsRead(notificationIds)
    }
    
    override suspend fun clearAll() {
        doorStatusDao.clear()
        notificationDao.clear()
    }
}
