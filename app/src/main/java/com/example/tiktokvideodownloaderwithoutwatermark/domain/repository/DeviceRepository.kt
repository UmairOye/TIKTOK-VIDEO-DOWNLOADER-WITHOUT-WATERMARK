package com.example.tiktokvideodownloaderwithoutwatermark.domain.repository

import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.MediaModel
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun getFoldersInDownloadPath(): Flow<List<FolderModel>>
    suspend fun getMediaFilesFromFolder(folderPath: String): Flow<List<MediaModel>>
}