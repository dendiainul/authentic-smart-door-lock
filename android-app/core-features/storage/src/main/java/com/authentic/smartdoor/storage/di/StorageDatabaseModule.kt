package com.authentic.smartdoor.storage.di

import android.content.Context
import androidx.room.Room
import com.authentic.smartdoor.storage.local.AppDatabase
import com.authentic.smartdoor.storage.local.dao.*
import com.authentic.smartdoor.storage.local.dao.AccessLogDao
import com.authentic.smartdoor.storage.local.dao.CameraRecordDao
import com.authentic.smartdoor.storage.local.dao.DoorStatusDao
import com.authentic.smartdoor.storage.local.dao.NotificationDao
import com.authentic.smartdoor.storage.local.dao.SystemSettingDao
import com.authentic.smartdoor.storage.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_door_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideAccessLogDao(db: AppDatabase): AccessLogDao = db.accessLogDao()
    @Provides fun provideDoorStatusDao(db: AppDatabase): DoorStatusDao = db.doorStatusDao()
    @Provides fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao()
    @Provides fun provideCameraRecordDao(db: AppDatabase): CameraRecordDao = db.cameraRecordDao()
    @Provides fun provideSystemSettingDao(db: AppDatabase): SystemSettingDao = db.systemSettingDao()
    
    @Provides fun provideDashboardLocalDataSource(
        doorStatusDao: DoorStatusDao,
        notificationDao: NotificationDao
    ): com.authentic.smartdoor.storage.local.datasource.DashboardLocalDataSource {
        return com.authentic.smartdoor.storage.local.datasource.DashboardLocalDataSourceImpl(
            doorStatusDao, notificationDao
        )
    }
}


