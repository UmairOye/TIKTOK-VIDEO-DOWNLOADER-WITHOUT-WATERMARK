package com.example.tiktokvideodownloaderwithoutwatermark.ui.downloaderViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.MediaModel
import com.example.tiktokvideodownloaderwithoutwatermark.domain.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(private val deviceRepository: DeviceRepository): ViewModel() {

    private val _downloadFolders = MutableStateFlow<List<FolderModel>>(emptyList())
    val downloadFolders = _downloadFolders.asStateFlow()


    private val _downloadVideos = MutableStateFlow<List<MediaModel>>(emptyList())
    val downloadVideos = _downloadVideos.asStateFlow()

    init {
        fetchFolders()
    }


    private fun fetchFolders(){
        viewModelScope.launch {
            deviceRepository.getFoldersInDownloadPath().collect{
                _downloadFolders.value = it
            }
        }
    }


    fun fetchVideoFiles(path: String){
        viewModelScope.launch {
            deviceRepository.getMediaFilesFromFolder(path).collect{
                _downloadVideos.value = it
            }
        }
    }


}