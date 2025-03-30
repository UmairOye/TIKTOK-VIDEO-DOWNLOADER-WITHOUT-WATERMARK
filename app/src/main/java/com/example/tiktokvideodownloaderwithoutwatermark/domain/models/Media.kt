package com.example.tiktokvideodownloaderwithoutwatermark.domain.models

data class Media(
    val url: String? = null,
    val quality: String? = null,
    val extension: String? = null,
    val size: Int? = null,
    val formattedSize: String? = null,
    val videoAvailable: Boolean? = null,
    val audioAvailable: Boolean? = null,
    val chunked: Boolean? = null,
    val cached: Boolean? = null,
    val requiresRendering: Boolean? = null)