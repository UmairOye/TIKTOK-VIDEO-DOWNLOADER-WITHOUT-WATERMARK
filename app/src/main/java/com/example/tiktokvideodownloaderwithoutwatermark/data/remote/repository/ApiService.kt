package com.example.tiktokvideodownloaderwithoutwatermark.data.remote.repository

import com.example.tiktokvideodownloaderwithoutwatermark.BuildConfig
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.VideoRequest
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.VideoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST(BuildConfig.END_POINTS)
    suspend fun getVideoData(@Body request: VideoRequest): Response<VideoResponse>
}