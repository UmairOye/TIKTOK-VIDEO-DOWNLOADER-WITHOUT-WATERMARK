package com.example.tiktokvideodownloaderwithoutwatermark.data.remote.viewModel

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ContentUris
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktokvideodownloaderwithoutwatermark.data.remote.repository.TiktokRepo
import com.example.tiktokvideodownloaderwithoutwatermark.models.MediaModel
import com.example.tiktokvideodownloaderwithoutwatermark.models.folderModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.AUDIOS
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.VIDEOS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class TiktokViewModel : ViewModel() {
    private val tiktokRepo: TiktokRepo = TiktokRepo()

    private val expandedUrlLiveData = tiktokRepo.getExpandedUrlLiveData()
    fun getExpandedUrlLiveData(): LiveData<String> {
        return expandedUrlLiveData
    }

    fun expandShortenedUrl(shortenedUrl: String) {
        viewModelScope.launch {
            tiktokRepo.expandShortenedUrl(shortenedUrl)
        }
    }

    fun extractVideoIdFromUrl(url: String): String? {
        val pattern = Regex("/video/(\\d+)")
        val matchResult = pattern.find(url)
        return matchResult?.groupValues?.getOrNull(1)
    }

    fun downloadVideo(filename: String, downloadUrlOfVideo: String, context: Context) {
        try {
            if (!VIDEOS.exists()) {
                VIDEOS.mkdirs()
            }

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(downloadUrlOfVideo)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("video/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(File(VIDEOS, "$filename")))

            dm.enqueue(request)

            Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed! ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    fun downloadAudio(filename: String, downloadUrlOfVideo: String, context: Context) {
        try {
            if (!AUDIOS.exists()) {
                AUDIOS.mkdirs()
            }

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(downloadUrlOfVideo)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("audio/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(File(AUDIOS, "$filename")))

            dm.enqueue(request)

            Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed! ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    fun showAlertDialog(heading: String, subHeading: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(heading)
            .setMessage(subHeading)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    suspend fun getVideosFromFolder(context: Context, folderPath: String): ArrayList<MediaModel> = withContext(Dispatchers.IO) {
        val videoList: ArrayList<MediaModel> = ArrayList()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )

        val selection = "${MediaStore.Video.Media.DATA} like ?"
        val selectionArgs = arrayOf("$folderPath%")

        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        val queryUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        context.contentResolver.query(queryUri, projection, selection, selectionArgs, sortOrder)
            ?.use { cursor ->
                while (cursor.moveToNext()) {
                    val videoId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                    val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        videoId
                    )
                    videoList.add(MediaModel(displayName, contentUri, formatDuration(duration), formatSize(size)))
                }
            }
        return@withContext videoList
    }

    suspend fun getAudiosFromFolder(context: Context, folderPath: String): ArrayList<MediaModel> = withContext(Dispatchers.IO) {
        val audioList: ArrayList<MediaModel> = ArrayList()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
        )

        val selection = "${MediaStore.Audio.Media.DATA} like ?"
        val selectionArgs = arrayOf("$folderPath%")

        val sortOrder = "${MediaStore.Audio.Media.DATE_MODIFIED} DESC"

        val queryUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        context.contentResolver.query(queryUri, projection, selection, selectionArgs, sortOrder)
            ?.use { cursor ->
                while (cursor.moveToNext()) {
                    val videoId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        videoId
                    )
                    audioList.add(MediaModel(displayName, contentUri, formatDuration(duration), formatSize(size)))
                }
            }
        return@withContext audioList
    }



    private fun formatSize(size: Long): String {
        val fileSizeInKB = size / 1024
        val fileSizeInMB = fileSizeInKB / 1024

        return if (fileSizeInMB > 0) {
            String.format("%d MB", fileSizeInMB)
        } else {
            String.format("%d KB", fileSizeInKB)
        }
    }

    private fun formatDuration(duration: Long): String {
        val seconds = duration / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d", minutes, remainingSeconds)
    }


    suspend fun getFolderInfo(): ArrayList<folderModel> = withContext(Dispatchers.IO) {
        val folderList: ArrayList<folderModel> = ArrayList()
        if (Utils.TIKTOK_DOWNLOAD_PATH.exists() && Utils.TIKTOK_DOWNLOAD_PATH.isDirectory) {
            val videosFolder = Utils.TIKTOK_DOWNLOAD_PATH.resolve("VIDEOS")
            val videosCount = getMediaCount(videosFolder)
            folderList.add(folderModel(videosFolder.name, videosCount))

            val audiosFolder = Utils.TIKTOK_DOWNLOAD_PATH.resolve("AUDIOS")
            val audiosCount = getMediaCount(audiosFolder)
            folderList.add(folderModel(audiosFolder.name, audiosCount))
        } else {
            Log.e("NotFound", "TikTok folder not found.")
        }
        return@withContext folderList
    }

    private fun getMediaCount(folder: File): Int {
        return folder.listFiles { file -> file.isFile }?.size ?: 0
    }


}