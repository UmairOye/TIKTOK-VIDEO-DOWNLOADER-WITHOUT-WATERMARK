package com.example.tiktokvideodownloaderwithoutwatermark.data.remote.repository

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import com.example.tiktokvideodownloaderwithoutwatermark.BuildConfig
import com.example.tiktokvideodownloaderwithoutwatermark.domain.repository.ApiRepository
import com.example.tiktokvideodownloaderwithoutwatermark.domain.requestStates.RequestState
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.VideoRequest
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.VideoResponse
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.TIKTOK_DOWNLOAD_PATH
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.VIDEOS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ApiRepositoryImplementation @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : ApiRepository {
    override suspend fun fetchVideos(videoUrl: String): RequestState<VideoResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response =
                    apiService.getVideoData(VideoRequest(videoUrl, BuildConfig.API_TOKEN))

                if (response.isSuccessful) {
                    response.body()?.let {
                        RequestState.Success(it)
                    } ?: RequestState.Error(Exception("Response body is null"))
                } else {
                    RequestState.Error(Exception("API call failed with code ${response.code()} and message: ${response.message()}"))
                }
            } catch (e: Exception) {
                RequestState.Error(e)
            }
        }

    override suspend fun decodeBase64ToBitmap(base64String: String): Bitmap? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val base64Image = base64String.substringAfter("base64,")
                val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override fun downloadVideo(filename: String, downloadUrlOfVideo: String) {
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
                .setDestinationUri(Uri.fromFile(File(VIDEOS, filename)))

            dm.enqueue(request)

            Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed! ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}