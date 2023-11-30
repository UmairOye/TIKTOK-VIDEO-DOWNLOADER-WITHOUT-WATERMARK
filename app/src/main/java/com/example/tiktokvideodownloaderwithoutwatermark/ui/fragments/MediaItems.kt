package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.example.tiktokvideodownloaderwithoutwatermark.adapters.DownloadAdapters
import com.example.tiktokvideodownloaderwithoutwatermark.data.remote.viewModel.TiktokViewModel
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentMediaItemsBinding
import com.example.tiktokvideodownloaderwithoutwatermark.models.MediaModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import kotlinx.coroutines.launch

class MediaItems : Fragment() {
    private var _binding: FragmentMediaItemsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TiktokViewModel by activityViewModels()
    private lateinit var adapter: DownloadAdapters
    private var list: ArrayList<MediaModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaItemsBinding.inflate(inflater, container, false)

        val arguments: String? = requireArguments().getString("MEDIA")

        adapter = DownloadAdapters()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        lifecycleScope.launch {
            when (arguments) {
                "audio" -> {
                    list =
                        viewModel.getAudiosFromFolder(requireContext(), Utils.AUDIOS.absolutePath)
                    adapter.submitList(list)
                }

                "video" -> {
                    list =
                        viewModel.getVideosFromFolder(requireContext(), Utils.VIDEOS.absolutePath)
                    adapter.submitList(list)
                }
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}