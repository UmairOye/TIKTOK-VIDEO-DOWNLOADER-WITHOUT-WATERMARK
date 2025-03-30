package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentHomeBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.requestStates.RequestState
import com.example.tiktokvideodownloaderwithoutwatermark.ui.downloaderViewModel.DownloaderViewModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DownloaderViewModel by activityViewModels()
    private var downloadedUrl: String? = null
    private var extension: String? = null
    private var source: String? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDownload.setOnClickListener {
            hideKeyboard(binding.root)
            activity?.let {
                if (binding.edUrl.text.toString().isNotEmpty()) {
                    viewModel.requestInstagramVideoDetails(binding.edUrl.text.toString())
                } else {
                    it.showToast("Please enter valid url")
                }
            }
        }

        binding.downloadVideo.setOnClickListener {
            resetStates()
            if (downloadedUrl != null && extension != null && source != null) {
                viewModel.downloadVideos(
                    downloadedUrl!!, ".$extension",
                    source!!
                )

                binding.downloadDetails.isVisible = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isBitmapFetched.collect{ isBitmapFetched->
                if(isBitmapFetched){
                   binding.downloadImage.setImageBitmap(viewModel.bitmap)
                }
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.instagramVideoDetail.collect { videoResponse ->
                when (videoResponse) {
                    is RequestState.Idle -> {
                        binding.includeShimmer.shimmerEffect.isVisible = false
                    }

                    is RequestState.Loading -> {
                        binding.includeShimmer.shimmerEffect.isVisible = true
                    }

                    is RequestState.Success -> {
                        binding.apply {
                            includeShimmer.shimmerEffect.isVisible = false
                            includeShimmer.shimmerEffect.stopShimmer()

                            downloadDetails.isVisible = true
                            tvQuality.text = videoResponse.data.medias?.get(0)?.quality
                            tvDuration.text = videoResponse.data.duration
                            tvSize.text = videoResponse.data.medias?.get(0)?.formattedSize
                            tvExtension.text = videoResponse.data.medias?.get(0)?.extension
                            tvSource.text = videoResponse.data.source
                        }

                        videoResponse.data.medias?.get(0)?.url?.let { url ->
                            downloadedUrl = url
                        }

                        source = videoResponse.data.source
                        extension = videoResponse.data.medias?.get(0)?.extension
                    }

                    is RequestState.Error -> {}
                }
            }
        }


        binding.clearText.setOnClickListener {
            binding.edUrl.setText("")
        }


        binding.btnPaste.setOnClickListener {
            val copyBoardText = Utils.pasteFromClipboard(requireContext())
            if (copyBoardText.isNotEmpty()) {
                binding.edUrl.setText(copyBoardText)
            } else {
                binding.edUrl.setText("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetStates()
        _binding = null
    }


    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun resetStates(){
        viewModel.resetStates()
        binding.apply {
            downloadDetails.isVisible = false
            edUrl.setText("")
        }
    }
}