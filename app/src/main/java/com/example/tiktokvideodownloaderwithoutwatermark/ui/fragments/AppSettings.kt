package com.example.tiktokvideodownloaderwithoutwatermark.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.example.tiktokvideodownloaderwithoutwatermark.data.remote.viewModel.TiktokViewModel
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentAppSettingsBinding
import com.example.tiktokvideodownloaderwithoutwatermark.models.folderModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils


class AppSettings : Fragment(){
    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TiktokViewModel by activityViewModels()
    private val list: ArrayList<folderModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)

        binding.storagePath.text = Utils.TIKTOK_DOWNLOAD_PATH.absolutePath
        binding.appVersion.text = "1.0"
        binding.storagePath.isSelected = true

        binding.cdPrivcyPolicy.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_action_settings_to_privacyPolicy)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        binding.cdShare.setOnClickListener { shareAppLink() }

        binding.cdRateUs.setOnClickListener { openPlayStore() }

        return binding.root
    }


    private fun shareAppLink() {
        val appLink =
            "https://play.google.com/store/apps/details?id=${requireContext().packageName}"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, appLink)
        startActivity(Intent.createChooser(intent, "Share App Link"))
    }

    private fun openPlayStore() {
        val appPackageName = requireActivity().packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}