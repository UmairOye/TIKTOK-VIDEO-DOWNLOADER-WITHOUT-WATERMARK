package com.example.tiktokvideodownloaderwithoutwatermark.domain.models

data class VideoResponse(
    val url: String? = null,
    val title: String? = null,
    val thumbnail: String? = null,
    val duration: String? = null,
    val source: String? = null,
    val medias: List<Media>? = null,
    val sid: String? = null
)