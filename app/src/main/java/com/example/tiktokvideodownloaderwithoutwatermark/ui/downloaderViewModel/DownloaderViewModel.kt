package com.example.tiktokvideodownloaderwithoutwatermark.ui.downloaderViewModel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.VideoResponse
import com.example.tiktokvideodownloaderwithoutwatermark.domain.repository.ApiRepository
import com.example.tiktokvideodownloaderwithoutwatermark.domain.requestStates.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloaderViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {
    private val _instagramVideoDetail = MutableStateFlow<RequestState<VideoResponse>>(RequestState.Idle)
    val instagramVideoDetail: StateFlow<RequestState<VideoResponse>> = _instagramVideoDetail.asStateFlow()
    var bitmap: Bitmap? = null
    private val _isBitmapFetched = MutableStateFlow(false)
    val isBitmapFetched = _isBitmapFetched.asStateFlow()






    fun requestInstagramVideoDetails(url: String){
        _instagramVideoDetail.value = RequestState.Loading
        viewModelScope.launch {
            val response = apiRepository.fetchVideos(videoUrl = url)
            when(response){
                is RequestState.Error -> TODO()
                RequestState.Idle -> TODO()
                RequestState.Loading -> TODO()
                is RequestState.Success -> {
                    response.data.thumbnail?.let { getThumbnail(it) }
                }
            }
            _instagramVideoDetail.value = response
        }
    }


    fun resetStates(){
        _instagramVideoDetail.value = RequestState.Idle
        _isBitmapFetched.value = false
    }

    private suspend fun getThumbnail(base64: String){
        val bitmapAsync = viewModelScope.async  {
            bitmap = apiRepository.decodeBase64ToBitmap(base64)
        }.await()

        _isBitmapFetched.value = true
    }


    fun downloadVideos(url: String, extension: String, source: String){
        val random = (0..10000).random()
        apiRepository.downloadVideo(filename = source.plus(random).plus(extension), downloadUrlOfVideo = url)
    }


}