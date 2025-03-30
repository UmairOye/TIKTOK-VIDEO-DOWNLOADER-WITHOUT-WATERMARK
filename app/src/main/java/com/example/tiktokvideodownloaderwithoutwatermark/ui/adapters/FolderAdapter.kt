package com.example.tiktokvideodownloaderwithoutwatermark.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokvideodownloaderwithoutwatermark.databinding.ListFoldersBinding
import com.example.tiktokvideodownloaderwithoutwatermark.domain.models.FolderModel
import java.io.File

class FolderAdapter :
    ListAdapter<FolderModel, FolderAdapter.FolderViewHolder>(FolderDiffCallback()) {
    private var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ListFoldersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.cdMain.setOnClickListener {
            listener?.onItemClick(item)
        }
    }

    class FolderViewHolder(private val binding: ListFoldersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cdMain = binding.cdMain
        fun bind(item: FolderModel) {
            binding.tvFolderName.text = File(item.name).name
            binding.tvNoOfItems.text = "(${item.noOfItems})"
        }
    }

    interface OnClickListener {
        fun onItemClick(item: FolderModel)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }
}

class FolderDiffCallback : DiffUtil.ItemCallback<FolderModel>() {
    override fun areItemsTheSame(oldItem: FolderModel, newItem: FolderModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FolderModel, newItem: FolderModel): Boolean {
        return oldItem == newItem
    }
}
