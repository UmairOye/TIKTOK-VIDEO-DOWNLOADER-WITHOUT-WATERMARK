package com.example.tiktokvideodownloaderwithoutwatermark.domain.repository

import android.graphics.Bitmap
import com.example.tiktokvideodownloaderwithoutwatermark.domain.requestStates.RequestState
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.VideoResponse

interface ApiRepository {
    suspend fun fetchVideos(videoUrl: String): RequestState<VideoResponse>
    suspend fun decodeBase64ToBitmap(base64String: String): Bitmap?
    fun downloadVideo(filename: String, downloadUrlOfVideo: String)
}