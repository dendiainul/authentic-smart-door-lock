package com.authentic.smartdoor.storage.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit
import com.authentic.smartdoor.storage.remote.api.AuthApiService
import com.authentic.smartdoor.storage.remote.api.DashboardApiService
import com.authentic.smartdoor.storage.remote.api.CameraApiService
import com.authentic.smartdoor.storage.network.TokenRefreshInterceptor
import com.authentic.smartdoor.storage.network.TokenAuthenticator
import com.authentic.smartdoor.storage.preferences.PreferencesManager
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object StorageNetworkModule {

    @Provides
    @Singleton
    @Named("baseClient")
    fun provideBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Named("mainClient")
    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenRefreshInterceptor: TokenRefreshInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(tokenRefreshInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(@Named("baseClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.122.163:5002/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Named("mainRetrofit")
    @Provides
    @Singleton
    fun provideRetrofit(@Named("mainClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.122.163:5002/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("authApi")
    fun provideAuthApiService(@Named("authRetrofit") retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiServiceDefault(@Named("mainRetrofit") retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideDashboardApiService(@Named("mainRetrofit") retrofit: Retrofit): DashboardApiService = retrofit.create(DashboardApiService::class.java)

    @Provides
    @Singleton
    fun provideCameraApiService(@Named("mainRetrofit") retrofit: Retrofit): CameraApiService = retrofit.create(CameraApiService::class.java)

    @Provides
    @Singleton
    fun provideTokenRefreshInterceptor(
        preferencesManager: PreferencesManager,
        @Named("authApi") authApi: AuthApiService
    ): TokenRefreshInterceptor {
        return TokenRefreshInterceptor(preferencesManager, authApi)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        preferencesManager: PreferencesManager,
        @Named("authApi") authApi: AuthApiService
    ): TokenAuthenticator {
        return TokenAuthenticator(preferencesManager, authApi)
    }
}


