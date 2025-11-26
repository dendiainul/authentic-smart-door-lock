package com.authentic.smartdoor.dashboard.di

import com.authentic.smartdoor.dashboard.data.repository.DashboardRepositoryImpl
import com.authentic.smartdoor.dashboard.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}


