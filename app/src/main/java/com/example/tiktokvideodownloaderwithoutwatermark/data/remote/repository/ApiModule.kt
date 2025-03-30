package com.example.tiktokvideodownloaderwithoutwatermark.data.remote.repository

import com.example.tiktokvideodownloaderwithoutwatermark.BuildConfig
import com.example.tiktokvideodownloaderwithoutwatermark.domain.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRepository(apiRepositoryImplementation: ApiRepositoryImplementation): ApiRepository {
        return apiRepositoryImplementation
    }
}