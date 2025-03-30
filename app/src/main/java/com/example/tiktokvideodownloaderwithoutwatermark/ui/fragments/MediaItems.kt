package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.tiktokvideodownloaderwithoutwatermark.ui.adapters.DownloadAdapters
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentMediaItemsBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.MediaModel
import com.example.tiktokvideodownloaderwithoutwatermark.ui.downloaderViewModel.DownloaderViewModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import kotlinx.coroutines.launch

class MediaItems : Fragment() {
    private var _binding: FragmentMediaItemsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DownloaderViewModel by activityViewModels()
    private lateinit var adapter: DownloadAdapters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DownloadAdapters()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}