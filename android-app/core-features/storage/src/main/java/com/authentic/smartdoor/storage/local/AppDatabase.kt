package com.authentic.smartdoor.storage.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.authentic.smartdoor.storage.local.dao.*
import com.authentic.smartdoor.storage.local.entities.*
import com.authentic.smartdoor.storage.local.dao.AccessLogDao
import com.authentic.smartdoor.storage.local.dao.CameraRecordDao
import com.authentic.smartdoor.storage.local.dao.DoorStatusDao
import com.authentic.smartdoor.storage.local.dao.NotificationDao
import com.authentic.smartdoor.storage.local.dao.SystemSettingDao
import com.authentic.smartdoor.storage.local.dao.UserDao
import com.authentic.smartdoor.storage.local.entities.AccessLogEntity
import com.authentic.smartdoor.storage.local.entities.CameraRecordEntity
import com.authentic.smartdoor.storage.local.entities.DoorStatusEntity
import com.authentic.smartdoor.storage.local.entities.NotificationEntity
import com.authentic.smartdoor.storage.local.entities.SystemSettingEntity
import com.authentic.smartdoor.storage.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        AccessLogEntity::class,
        DoorStatusEntity::class,
        NotificationEntity::class,
        CameraRecordEntity::class,
        SystemSettingEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accessLogDao(): AccessLogDao
    abstract fun doorStatusDao(): DoorStatusDao
    abstract fun notificationDao(): NotificationDao
    abstract fun cameraRecordDao(): CameraRecordDao
    abstract fun systemSettingDao(): SystemSettingDao
}


