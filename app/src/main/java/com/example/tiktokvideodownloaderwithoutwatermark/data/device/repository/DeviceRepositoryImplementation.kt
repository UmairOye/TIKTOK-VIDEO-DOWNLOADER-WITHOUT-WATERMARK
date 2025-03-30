package com.example.tiktokvideodownloaderwithoutwatermark.data.device.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.MediaModel
import com.example.tiktokvideodownloaderwithoutwatermark.domain.repository.DeviceRepository
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.TIKTOK_DOWNLOAD_PATH
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject

class DeviceRepositoryImplementation @Inject constructor(@ApplicationContext private val context: Context) :
    DeviceRepository {


    override suspend fun getFoldersInDownloadPath(): Flow<List<FolderModel>> =
        flow {
            val folderMap = mutableMapOf<String, Int>()
            val collectionUri = MediaStore.Files.getContentUri("external")

            val projection = arrayOf(
                MediaStore.Files.FileColumns.DATA
            )

            val selection = "${MediaStore.Files.FileColumns.DATA} LIKE ?"
            val selectionArgs = arrayOf("${TIKTOK_DOWNLOAD_PATH.absolutePath}/%")

            context.contentResolver.query(collectionUri, projection, selection, selectionArgs, null)
                ?.use { cursor ->
                    val columnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)

                    while (cursor.moveToNext()) {
                        val filePath = cursor.getString(columnIndex)
                        val folderPath = File(filePath).parent
                        folderPath?.let { folderMap[it] = folderMap.getOrDefault(it, 0) + 1 }
                    }
                }

            emit(folderMap.map { FolderModel(it.key, it.value) })
        }.flowOn(Dispatchers.IO)



    override suspend fun getMediaFilesFromFolder(folderPath: String): Flow<List<MediaModel>> = flow {
        val mediaList = mutableListOf<MediaModel>()
        val collectionUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )

        val selection = "${MediaStore.Video.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("$folderPath/%")

        context.contentResolver.query(collectionUri, projection, selection, selectionArgs, null)
            ?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                val dataIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
                val durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                val sizeIndex = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)

                while (cursor.moveToNext()) {
                    val name = cursor.getString(nameIndex)
                    val filePath = cursor.getString(dataIndex)
                    val duration = cursor.getLong(durationIndex).toString()
                    val size = cursor.getLong(sizeIndex).toString()

                    val fileUri = Uri.fromFile(File(filePath))
                    mediaList.add(MediaModel(name, fileUri, duration, size))
                }
            }

        emit(mediaList)
    }.flowOn(Dispatchers.IO)

}