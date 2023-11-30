package com.example.tiktokvideodownloaderwithoutwatermark.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String)
{
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}


