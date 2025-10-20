package com.authentic.smartdoor.storage.di

import com.authentic.smartdoor.storage.local.datasource.AuthLocalDataSource
import com.authentic.smartdoor.storage.local.datasource.AuthLocalDataSourceImpl
import com.authentic.smartdoor.storage.remote.datasource.AuthRemoteDataSource
import com.authentic.smartdoor.storage.remote.datasource.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageAuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(impl: AuthLocalDataSourceImpl): AuthLocalDataSource
}


