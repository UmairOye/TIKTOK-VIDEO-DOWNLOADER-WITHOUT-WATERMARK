package com.example.tiktokvideodownloaderwithoutwatermark.data.device.repository

import com.example.tiktokvideodownloaderwithoutwatermark.domain.repository.DeviceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceModule {


    @Provides
    @Singleton
    fun provideDeviceRepo(deviceRepositoryImplementation: DeviceRepositoryImplementation): DeviceRepository{
        return deviceRepositoryImplementation
    }
}