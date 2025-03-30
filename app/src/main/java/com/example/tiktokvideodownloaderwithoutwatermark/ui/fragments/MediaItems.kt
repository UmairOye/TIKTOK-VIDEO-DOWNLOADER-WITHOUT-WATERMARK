package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentMediaItemsBinding
import com.example.tiktokvideodownloaderwithoutwatermark.ui.adapters.DownloadAdapter
import com.example.tiktokvideodownloaderwithoutwatermark.ui.downloaderViewModel.DeviceViewModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import kotlinx.coroutines.launch

class MediaItems : Fragment() {
    private var _binding: FragmentMediaItemsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeviceViewModel by activityViewModels()
    private val adapter = DownloadAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchVideoFiles(Utils.VIDEOS.absolutePath)
            viewModel.downloadVideos.collect { videoList ->
                if(videoList.isNotEmpty()){
                    binding.pb.isVisible = false
                    adapter.submitList(videoList)
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}