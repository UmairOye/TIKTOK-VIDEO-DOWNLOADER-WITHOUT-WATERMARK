package com.example.tiktokvideodownloaderwithoutwatermark.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.VideoItemsBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.MediaModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.showAudio
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.showVideo

class DownloadAdapters : RecyclerView.Adapter<DownloadAdapters.DownloadViewHolder>() {
    private var downloadList: ArrayList<MediaModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding =
            VideoItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return downloadList.size
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.bind(downloadList[position])
    }

    class DownloadViewHolder(private val binding: VideoItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaModel) {
            Glide.with(binding.root.context).load(item.uri)
                .placeholder(R.drawable.music).into(binding.tvImgMedia)
            binding.tvVideoName.text = item.name
            binding.tvVideoName.isSelected = true
            (item.size + " - " + item.duration).also { binding.tvVideoDetails.text = it }

            binding.imgMore.setOnClickListener {
                showPopupMenu(binding.imgMore, binding.root.context, item.uri)
            }

        }

        private fun showPopupMenu(view: View, context: Context, uri: Uri) {
            val popupMenu = PopupMenu(context, view, Gravity.START)
            popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)
            val isVideo = Utils.isVideoFile(context, uri)

            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.action_share -> {
                        if (isVideo) {
                            Utils.shareVideo(context, uri)
                        } else {
                            Utils.shareAudio(context, uri)
                        }
                        true
                    }

                    R.id.action_view -> {
                        if (isVideo) {
                            showVideo(uri, context)
                        } else {
                            showAudio(uri, context)
                        }
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    fun submitList(list: ArrayList<MediaModel>) {
        this.downloadList = list
        notifyDataSetChanged()
    }


}