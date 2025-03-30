package com.example.tiktokvideodownloaderwithoutwatermark.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.showToast(message: String)
{
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


