package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.example.tiktokvideodownloaderwithoutwatermark.adapters.FolderAdapter
import com.example.tiktokvideodownloaderwithoutwatermark.data.remote.viewModel.TiktokViewModel
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentDownloadBinding
import com.example.tiktokvideodownloaderwithoutwatermark.models.folderModel
import kotlinx.coroutines.launch

class Download : Fragment() {
    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TiktokViewModel by activityViewModels()
    private lateinit var adapter: FolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)

        adapter = FolderAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        lifecycleScope.launch {
            val list = viewModel.getFolderInfo()
            if (list.isNotEmpty()) {
                binding.textView2.visibility = View.GONE
                adapter.submitList(list)
            }
        }

        adapter.setOnClickListener(listener = object : FolderAdapter.OnClickListener {
            override fun onItemClick(item: folderModel) {
                try {
                    val bundle = Bundle()
                    when (item.name) {
                        "AUDIOS" -> {
                            bundle.putString("MEDIA", "audio")
                            findNavController().navigate(
                                R.id.action_action_download_to_mediaItems,
                                bundle
                            )
                        }

                        "VIDEOS" -> {
                            bundle.putString("MEDIA", "video")
                            findNavController().navigate(
                                R.id.action_action_download_to_mediaItems,
                                bundle
                            )
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        })

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}