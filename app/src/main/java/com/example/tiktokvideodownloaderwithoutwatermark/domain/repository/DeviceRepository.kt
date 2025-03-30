package com.example.tiktokvideodownloaderwithoutwatermark.domain.repository

import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    suspend fun getFoldersInDownloadPath(): Flow<List<FolderModel>>
}