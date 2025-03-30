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
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.FragmentAppSettingsBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import com.example.tiktokvideodownloaderwithoutwatermark.utils.showToast


class AppSettings : Fragment(){
    private var _binding: FragmentAppSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            storagePath.text = Utils.TIKTOK_DOWNLOAD_PATH.absolutePath
            appVersion.text = getString(R.string._1_1)
            storagePath.isSelected = true
            cdShare.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("askedumairbashir@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Github Project")
                    putExtra(Intent.EXTRA_TEXT, "Hi Umair!")
                }

                activity?.let {
                    if (emailIntent.resolveActivity(it.packageManager) != null) {
                        startActivity(emailIntent)
                    } else {
                        activity?.showToast("No email app found")
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}