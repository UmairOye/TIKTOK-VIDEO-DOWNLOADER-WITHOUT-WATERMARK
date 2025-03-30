package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.example.tiktokvideodownloaderwithoutwatermark.ui.adapters.FolderAdapter
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentDownloadBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel
import com.example.tiktokvideodownloaderwithoutwatermark.ui.downloaderViewModel.DeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Download : Fragment() {
    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeviceViewModel by activityViewModels()
    private lateinit var adapter: FolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FolderAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.downloadFolders.collect{ folderList ->
                if(folderList.isNotEmpty()){
                    binding.pb.isVisible = false
                    adapter.submitList(folderList)
                }else{
                    binding.textView2.isVisible = true
                }
            }
        }

        adapter.setOnClickListener(listener = object : FolderAdapter.OnClickListener {
            override fun onItemClick(item: FolderModel) {
                try {
                    findNavController().navigate(
                        R.id.action_action_download_to_mediaItems)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}