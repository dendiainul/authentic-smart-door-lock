package com.authentic.smartdoor.storage.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.authentic.smartdoor.storage.local.entities.*
import com.authentic.smartdoor.storage.local.entities.AccessLogEntity
import com.authentic.smartdoor.storage.local.entities.CameraRecordEntity
import com.authentic.smartdoor.storage.local.entities.DoorStatusEntity
import com.authentic.smartdoor.storage.local.entities.NotificationEntity
import com.authentic.smartdoor.storage.local.entities.SystemSettingEntity
import com.authentic.smartdoor.storage.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE google_id = :googleId LIMIT 1")
    suspend fun getUserByGoogleId(googleId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface AccessLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AccessLogEntity)

    @Query("SELECT * FROM access_logs WHERE user_id = :userId ORDER BY timestamp DESC")
    suspend fun getLogsForUser(userId: String): List<AccessLogEntity>

    @Query("DELETE FROM access_logs")
    suspend fun clear()
}

@Dao
interface DoorStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatus(status: DoorStatusEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStatuses(statuses: List<DoorStatusEntity>)

    @Query("SELECT * FROM door_status ORDER BY last_update DESC LIMIT 1")
    suspend fun getLatestStatus(): DoorStatusEntity?

    @Query("SELECT * FROM door_status ORDER BY name ASC")
    suspend fun getAllStatuses(): List<DoorStatusEntity>

    @Query("DELETE FROM door_status")
    suspend fun clear()
}

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notifications WHERE user_id = :userId ORDER BY created_at DESC")
    suspend fun getNotificationsForUser(userId: String): List<NotificationEntity>

    @Query("SELECT * FROM notifications ORDER BY created_at DESC")
    fun getNotificationsFlow(): Flow<List<NotificationEntity>>

    @Query("UPDATE notifications SET read = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE notifications SET read = 1 WHERE id IN (:ids)")
    suspend fun markNotificationsAsRead(ids: List<String>)

    @Query("DELETE FROM notifications")
    suspend fun clear()
}

@Dao
interface CameraRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CameraRecordEntity)

    @Query("SELECT * FROM camera_records ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentRecords(limit: Int = 50): List<CameraRecordEntity>

    @Query("DELETE FROM camera_records")
    suspend fun clear()
}

@Dao
interface SystemSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: SystemSettingEntity)

    @Query("SELECT * FROM system_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingByKey(key: String): SystemSettingEntity?

    @Query("UPDATE system_settings SET value = :value, updated_at = :updatedAt, updated_by = :updatedBy WHERE `key` = :key")
    suspend fun updateSettingValue(key: String, value: String, updatedAt: Long, updatedBy: String)

    @Query("DELETE FROM system_settings")
    suspend fun clear()
}


