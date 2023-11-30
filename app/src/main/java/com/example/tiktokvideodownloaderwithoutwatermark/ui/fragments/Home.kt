package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.example.tiktokvideodownloaderwithoutwatermark.data.remote.viewModel.TiktokViewModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.TIKTOK_DOWNLOAD_PATH
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentHomeBinding
import com.example.tiktokvideodownloaderwithoutwatermark.utils.showToast
import kotlinx.coroutines.launch

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TiktokViewModel by activityViewModels()
    private var videoId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnDownload.setOnClickListener {
            hideKeyboard(binding.root)
            startReadExternalStoragePermission()
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


        binding.cdAudio.setOnClickListener {
            viewModel.downloadAudio(
                "Video-$videoId.mp3",
                "${Utils.DOWNLOAD_AUDIO_LINK}$videoId.mp3",
                requireContext()
            )
        }

        binding.cdOriginalVideo.setOnClickListener {
            viewModel.downloadVideo(
                "Video-Original-$videoId.mp4",
                "${Utils.DOWNLOAD_ORIGINAL_VIDEO_LINK}$videoId.mp4",
                requireContext()
            )
        }

        binding.cdDownloadWithoutWaterMark.setOnClickListener {
            viewModel.downloadVideo(
                "Video-WM-$videoId.mp4",
                "${Utils.DOWNLOAD_VIDEO_WITHOUT_WATERMARK}$videoId.mp4",
                requireContext()
            )
        }

        binding.cdDownloadWithoutWaterMarkHD.setOnClickListener {
            viewModel.downloadVideo(
                "Video-WMHD-$videoId.mp4",
                "${Utils.DOWNLOAD_VIDEO_WITHOUT_WATERMARK_HD}$videoId.mp4",
                requireContext()
            )
        }


        binding.downloadAnother.setOnClickListener {
            binding.cdDownloadOptions.visibility = View.GONE
        }




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startReadExternalStoragePermission() {
        this.requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    private val requestReadExternalPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startWriteExternalStoragePermission()
        } else {
            viewModel.showAlertDialog(
                getString(R.string.media_permission),
                getString(R.string.please_allow_media_permission_to_continue),
                requireContext()
            )
        }
    }

    private fun startWriteExternalStoragePermission() {
        this.requestWriteExternalPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val requestWriteExternalPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (binding.edUrl.text.toString().isNotEmpty()) {
                lifecycleScope.launch {
                    if (Utils.isValidUrl(binding.edUrl.text.toString())) {

                        val dialogBuilder = AlertDialog.Builder(context)
                        val inflater = requireActivity().layoutInflater
                        val dialogView: View =
                            inflater.inflate(R.layout.fetching_dialog, null, false)
                        dialogBuilder.setView(dialogView)
                        val alertDialog = dialogBuilder.create()
                        alertDialog.setCancelable(false)



                        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation



                        alertDialog.show()
                        val window: Window? = alertDialog.window
                        window!!.setLayout(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )



                        viewModel.expandShortenedUrl(binding.edUrl.text.toString())
                        viewModel.getExpandedUrlLiveData()
                            .observe(viewLifecycleOwner) { expandedUrl ->
                                alertDialog.dismiss()
                                if (expandedUrl == "Error:" || expandedUrl == "Timeout occurred:") {
                                    showToast(expandedUrl.toString())
                                    return@observe
                                } else {
                                    videoId = viewModel.extractVideoIdFromUrl(expandedUrl)
                                    if (!TIKTOK_DOWNLOAD_PATH.exists()) {
                                        TIKTOK_DOWNLOAD_PATH.mkdir()
                                    }
                                    Glide.with(requireContext())
                                        .load("${Utils.SOURCE_IMAGEVIEW}$videoId.webp")
                                        .into(binding.srcImage)

                                    binding.edUrl.setText("")
                                    binding.cdDownloadOptions.visibility = View.VISIBLE
                                }
                            }

                    } else {
                        showToast("Invalid url, please check again that pasted url is of tiktok video.")
                    }
                }
            } else {
                showToast("url is not valid!")
            }
        } else {

            viewModel.showAlertDialog(
                getString(R.string.media_permission),
                getString(R.string.please_allow_media_permission_to_continue),
                requireContext()

            )
        }
    }


    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}