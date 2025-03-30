package com.example.tiktokvideodownloaderwithoutwatermark.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiktokvideodownloaderwithoutwatermark.R
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.VideoItemsBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.MediaModel
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.showAudio
import com.example.tiktokvideodownloaderwithoutwatermark.utils.Utils.showVideo

class DownloadAdapter :
    ListAdapter<MediaModel, DownloadAdapter.DownloadViewHolder>(DownloadDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding = VideoItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DownloadViewHolder(private val binding: VideoItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaModel) {
            Glide.with(binding.root.context).load(item.uri)
                .placeholder(R.drawable.music).into(binding.tvImgMedia)
            binding.tvVideoName.text = item.name
            binding.tvVideoName.isSelected = true
            binding.tvVideoDetails.text = "${item.size} - ${item.duration}"

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
}

class DownloadDiffCallback : DiffUtil.ItemCallback<MediaModel>() {
    override fun areItemsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(oldItem: MediaModel, newItem: MediaModel): Boolean {
        return oldItem == newItem
    }
}
