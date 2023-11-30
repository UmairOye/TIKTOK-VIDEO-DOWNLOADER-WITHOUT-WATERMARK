package com.example.tiktokvideodownloaderwithoutwatermark.utils

import android.app.Activity
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.io.File
import java.net.MalformedURLException
import java.net.URL

object Utils {
    val DOWNLOAD_AUDIO_LINK = "https://www.tikwm.com/video/music/"
    val DOWNLOAD_ORIGINAL_VIDEO_LINK = "https://www.tikwm.com/video/media/wmplay/"
    val DOWNLOAD_VIDEO_WITHOUT_WATERMARK = "https://www.tikwm.com/video/media/play/"
    val DOWNLOAD_VIDEO_WITHOUT_WATERMARK_HD = "https://www.tikwm.com/video/media/hdplay/"
    val SOURCE_IMAGEVIEW = "https://www.tikwm.com/video/cover/"


    val TIKTOK_DOWNLOAD_PATH = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "TIKTOK VIDEO DOWNLOADER"
    )

    val VIDEOS = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "TIKTOK VIDEO DOWNLOADER/VIDEOS"
    )

    val AUDIOS = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "TIKTOK VIDEO DOWNLOADER/AUDIOS"
    )

    fun isValidUrl(urlString: String): Boolean {
        return try {
            val url = URL(urlString)
            url.protocol == "http" || url.protocol == "https"
            true
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            false
        }
    }

    fun shareVideo(context: Context, videoUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "video/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
    }


    fun showVideo(uri: Uri, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setDataAndType(uri, "video/*")

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No app to handle video playback", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun showAudio(uri: Uri, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setDataAndType(uri, "audio/*")

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No app to handle video playback", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun shareAudio(context: Context, audioUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "audio/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, audioUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, "Share Audio"))
    }

    fun pasteFromClipboard(context: Context): String {
        var clipBoardText: String? = null
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip()) {
            val clip = clipboard.primaryClip
            val item = clip?.getItemAt(0)
            clipBoardText = item?.text.toString()
        }
        return clipBoardText!!
    }


    fun isVideoFile(context: Context, uri: Uri): Boolean {
        val mimeType = getMimeType(context, uri)
        return mimeType?.startsWith("video/") == true
    }

    fun isAudioFile(context: Context, uri: Uri): Boolean {
        val mimeType = getMimeType(context, uri)
        return mimeType?.startsWith("audio/") == true
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver
        return try {
            contentResolver.getType(uri)
        } catch (e: Exception) {
            null
        }
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}